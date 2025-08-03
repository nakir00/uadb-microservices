import {
  Calendar,
  CalendarDays,
  Euro,
  Eye,
  Home,
  MoreHorizontal,
  SquarePen,
  Trash,
  User,
  FileText,
} from 'lucide-react'
import { useState } from 'react'
import { toast } from 'sonner'
import { Link, useNavigate } from '@tanstack/react-router'
import type { ColumnDef } from '@tanstack/react-table'
import type { ContratModel } from '@/api/queries/contrat'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { api } from '@/api/api'
import { publish } from '@/lib/events'
import { useUser } from '@/hooks/user'

// Fonction utilitaire pour formater les dates en français
const formatDate = (dateString: string): string => {
  return new Date(dateString).toLocaleDateString('fr-FR', {
    day: '2-digit',
    month: 'long',
    year: 'numeric',
  })
}

// Fonction utilitaire pour calculer la durée du contrat
const calculateDuration = (dateDebut: string, dateFin: string): string => {
  const debut = new Date(dateDebut)
  const fin = new Date(dateFin)
  const diffTime = Math.abs(fin.getTime() - debut.getTime())
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  
  if (diffDays < 30) {
    return `${diffDays} jour(s)`
  } else if (diffDays < 365) {
    const months = Math.round(diffDays / 30)
    return `${months} mois`
  } else {
    const years = Math.round(diffDays / 365)
    return `${years} an(s)`
  }
}

// Fonction utilitaire pour le badge de statut
const getStatutBadge = (statut: ContratModel['statut']) => {
  const variants = {
    ACTIF: {
      variant: 'default' as const,
      text: 'Actif',
      color: 'bg-green-100 text-green-800',
    },
    RESILIE: {
      variant: 'destructive' as const,
      text: 'Résilié',
      color: 'bg-red-100 text-red-800',
    },
  }

  const config = variants[statut]
  return <Badge className={config.color}>{config.text}</Badge>
}

// Fonction utilitaire pour le badge de périodicité
const getPeriodiciteLabel = (periodicite: ContratModel['periodicite']) => {
  const labels = {
    JOURNALIER: 'Journalier',
    HEBDOMADAIRE: 'Hebdomadaire',
    MENSUEL: 'Mensuel',
  }
  return labels[periodicite]
}

// Fonction utilitaire pour le badge de mode de paiement
const getModePaiementLabel = (modePaiement: ContratModel['modePaiement']) => {
  const labels = {
    VIREMENT: 'Virement',
    CASH: 'Espèces',
    MOBILEMONEY: 'Mobile Money',
  }
  return labels[modePaiement]
}

export const ContratColumns: Array<ColumnDef<ContratModel>> = [
  {
    accessorKey: 'dateDebut',
    header: 'Date début',
    cell: ({ row }) => (
      <div className="flex items-center gap-2">
        <Calendar className="w-4 h-4 text-gray-500" />
        <span>{formatDate(row.getValue('dateDebut'))}</span>
      </div>
    ),
  },
  {
    accessorKey: 'dateFin',
    header: 'Date fin',
    cell: ({ row }) => (
      <div className="flex items-center gap-2">
        <CalendarDays className="w-4 h-4 text-gray-500" />
        <span>{formatDate(row.getValue('dateFin'))}</span>
      </div>
    ),
  },
  {
    id: 'duree',
    header: 'Durée',
    cell: ({ row }) => {
      const contrat = row.original
      return (
        <div className="text-sm font-medium">
          {calculateDuration(contrat.dateDebut, contrat.dateFin)}
        </div>
      )
    },
  },
  {
    accessorKey: 'moisCaution',
    header: 'Mois caution',
    cell: ({ row }) => (
      <div className="text-center">
        <Badge variant="outline">
          {row.getValue('moisCaution')} mois
        </Badge>
      </div>
    ),
  },
  {
    accessorKey: 'periodicite',
    header: 'Périodicité',
    cell: ({ row }) => (
      <Badge variant="secondary">
        {getPeriodiciteLabel(row.getValue('periodicite'))}
      </Badge>
    ),
  },
  {
    accessorKey: 'modePaiement',
    header: 'Mode paiement',
    cell: ({ row }) => (
      <div className="text-sm">
        {getModePaiementLabel(row.getValue('modePaiement'))}
      </div>
    ),
  },
  {
    accessorKey: 'statut',
    header: 'Statut',
    cell: ({ row }) => getStatutBadge(row.getValue('statut')),
  },
  {
    id: 'contrat-details',
    header: 'Détails',
    cell: ({ row }) => {
      const contrat = row.original
      const { user } = useUser()
      
      if (!contrat.id) {
        return (
          <Button disabled size="sm">
            Voir Contrat
          </Button>
        )
      }

      return (
        <Link
          to={user!.role==='ROLE_PROPRIETAIRE'?'/proprietaire/contrats/$contratId':'/locataire/contrats/$contratId'}
          params={{ contratId: contrat.id.toString() }}
        >
          <Button size="sm" variant="outline">
            <Eye className="w-4 h-4 mr-1" />
            Voir 
          </Button>
        </Link>
      )
    },
  },
  {
    id: 'actions',
    header: 'Actions',
    cell: ({ row }) => {
      const contrat = row.original
      const [open, setOpen] = useState(false)
      const [triggered, setTriggered] = useState('')
      const { user } = useUser()

      // Hooks pour les mutations API
      const contratUpdate = api.contrat.update()
      const contratDelete = api.contrat.delete()

      function closeDialog() {
        setOpen(false)
      }

      const deleteContrat = () => {
        if (!contrat.id) {
          toast.error('Erreur !!!', {
            description: 'Contrat non défini',
          })
          return
        }

        contratDelete.mutate(contrat.id, {
          onSettled() {
            closeDialog()
            publish('refresh_proprietaire_contrat_table', {})
          },
          onSuccess() {
            toast.success('Suppression effectuée', {
              description: 'La suppression du contrat est un succès',
            })
          },
          onError() {
            toast.error('Erreur !!!', {
              description: "Une erreur s'est produite lors de la suppression",
            })
          },
        })
      }

      const updateStatut = (nouveauStatut: ContratModel['statut']) => {
        if (!contrat.id) {
          toast.error('Erreur !!!', {
            description: 'Contrat non défini',
          })
          return
        }

        const formData = new FormData()
        formData.append('statut', nouveauStatut)

        contratUpdate.mutate(
          { data: formData, id: contrat.id },
          {
            onSettled() {
              closeDialog()
              publish('refresh_proprietaire_contrat_table', {})
            },
            onSuccess() {
              toast.success('Modification effectuée', {
                description: 'Le statut du contrat a été mis à jour',
              })
            },
            onError() {
              toast.error('Erreur !!!', {
                description:
                  "Une erreur s'est produite lors de la modification",
              })
            },
          },
        )
      }

      return (
        <Dialog open={open} onOpenChange={setOpen}>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" className="w-8 h-8 p-0">
                <span className="sr-only">Ouvrir le menu</span>
                <MoreHorizontal className="w-4 h-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuLabel>Actions</DropdownMenuLabel>

              {user!.role === 'ROLE_PROPRIETAIRE' &&
                contrat.statut === 'ACTIF' && (
                  <DropdownMenuItem
                    className="flex flex-row px-2 justify-between text-orange-600"
                    onClick={() => updateStatut('RESILIE')}
                  >
                    <span>Résilier</span>
                    <SquarePen />
                  </DropdownMenuItem>
                )}

              {user!.role === 'ROLE_PROPRIETAIRE' &&
                contrat.statut === 'RESILIE' && (
                  <DropdownMenuItem
                    className="flex flex-row px-2 justify-between text-green-600"
                    onClick={() => updateStatut('ACTIF')}
                  >
                    <span>Réactiver</span>
                    <SquarePen />
                  </DropdownMenuItem>
                )}

              <Link
                to={
                  user!.role === 'ROLE_LOCATAIRE'
                    ? '/locataire/contrats/$contratId/paiements'
                    : '/proprietaire/contrats/$contratId/paiements'
                }
                params={{ contratId: contrat.id?.toString() || '' }}
              >
                <DropdownMenuItem className="flex flex-row px-2 justify-between text-blue-600">
                  <span>Paiements</span>
                  <FileText />
                </DropdownMenuItem>
              </Link>

              {user!.role === 'ROLE_PROPRIETAIRE' && (
                <DialogTrigger asChild>
                  <DropdownMenuItem
                    className="flex flex-row px-2 justify-between text-red-500"
                    onClick={() => setTriggered('delete')}
                  >
                    Supprimer
                    <Trash />
                  </DropdownMenuItem>
                </DialogTrigger>
              )}
            </DropdownMenuContent>
          </DropdownMenu>

          <DialogContent>
            {triggered === 'delete' && (
              <>
                <DialogHeader>
                  <DialogTitle>Suppression Contrat</DialogTitle>
                  <DialogDescription>
                    Êtes-vous sûr de vouloir supprimer ce contrat #{contrat.id} ?
                  </DialogDescription>
                </DialogHeader>
                <div className="py-4">
                  <div className="text-sm text-gray-600">
                    <p><strong>Locataire:</strong> #{contrat.locataireId}</p>
                    <p><strong>Chambre:</strong> #{contrat.chambreId}</p>
                    <p><strong>Période:</strong> {formatDate(contrat.dateDebut)} - {formatDate(contrat.dateFin)}</p>
                    <p><strong>Montant caution:</strong> {contrat.montantCaution.toLocaleString('fr-FR')} FCFA</p>
                    <p><strong>Statut:</strong> {contrat.statut}</p>
                  </div>
                </div>
                <DialogFooter>
                  <div className="flex flex-row w-full gap-4">
                    <Button
                      className="w-1/3"
                      variant="destructive"
                      onClick={deleteContrat}
                    >
                      OUI
                    </Button>
                    <Button
                      className="w-2/3"
                      variant="outline"
                      onClick={closeDialog}
                    >
                      NON
                    </Button>
                  </div>
                </DialogFooter>
              </>
            )}
          </DialogContent>
        </Dialog>
      )
    },
  },
]