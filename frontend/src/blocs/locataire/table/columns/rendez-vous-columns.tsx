import {
  Calendar,
  Clock,
  Eye,
  Home,
  MoreHorizontal,
  SquarePen,
  Trash,
  User,
} from 'lucide-react'
import { useState } from 'react'
import { toast } from 'sonner'
import { Link, useNavigate } from '@tanstack/react-router'
import type { ColumnDef } from '@tanstack/react-table'
import type { RendezVousModel } from '@/api/queries/rendez-vous'
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
    hour: '2-digit',
    minute: '2-digit',
  })
}

// Fonction utilitaire pour formater la date en français
const formatDateOnly = (dateTimeString: string): string => {
  if (!dateTimeString) return 'Non défini'
  return new Date(dateTimeString).toLocaleDateString('fr-FR', {
    weekday: 'long',
    day: '2-digit',
    month: 'long',
    year: 'numeric',
  })
}

// Fonction utilitaire pour formater l'heure
const formatTimeOnly = (dateTimeString: string): string => {
  if (!dateTimeString) return 'Non défini'
  return new Date(dateTimeString).toLocaleTimeString('fr-FR', {
    hour: '2-digit',
    minute: '2-digit',
  })
}

// Fonction utilitaire pour le badge de statut
const getStatutBadge = (statut: RendezVousModel['statut']) => {
  const variants = {
    EN_ATTENTE: {
      variant: 'secondary' as const,
      text: 'En attente',
      color: 'bg-yellow-100 text-yellow-800',
    },
    CONFIRME: {
      variant: 'default' as const,
      text: 'Confirmé',
      color: 'bg-green-100 text-green-800',
    },
    ANNULE: {
      variant: 'destructive' as const,
      text: 'Annulé',
      color: 'bg-red-100 text-red-800',
    },
  }

  const config = variants[statut]
  return <Badge className={config.color}>{config.text}</Badge>
}

export const RendezVousColumns: Array<ColumnDef<RendezVousModel>> = [
  {
    accessorKey: 'dateHeure',
    header: 'Date',
    cell: ({ row }) => (
      <div className="flex items-center gap-2">
        <Calendar className="w-4 h-4 text-gray-500" />
        <span>{formatDateOnly(row.getValue('dateHeure'))}</span>
      </div>
    ),
  },
  {
    accessorKey: 'dateHeure',
    header: 'Heure',
    cell: ({ row }) => (
      <div className="flex items-center gap-2">
        <Clock className="w-4 h-4 text-gray-500" />
        <span>{formatTimeOnly(row.getValue('dateHeure'))}</span>
      </div>
    ),
  },
  {
    accessorKey: 'statut',
    header: 'Statut',
    cell: ({ row }) => getStatutBadge(row.getValue('statut')),
  },
  {
    accessorKey: 'chambre.titre',
    header: 'Chambre',
    cell: ({ row }) => {
      const chambre = row.original.chambre
      return (
        <div className="max-w-[200px]">
          <div className="font-medium truncate">{chambre.titre}</div>
          <div className="text-sm text-gray-500 truncate">
            {chambre.description}
          </div>
        </div>
      )
    },
  },
  {
    accessorKey: 'chambre.prix',
    header: 'Prix',
    cell: ({ row }) => {
      const prix = row.original.chambre.prix
      return (
        <div className="font-medium">{prix.toLocaleString('fr-FR')} FCFA</div>
      )
    },
  },
  {
    accessorKey: 'chambre.maison.adresse',
    header: 'Adresse',
    cell: ({ row }) => {
      const maison = row.original.chambre.maison
      return (
        <div className="flex items-center gap-2 max-w-[150px]">
          <Home className="w-4 h-4 text-gray-500 flex-shrink-0" />
          <span className="truncate">{maison!.adresse}</span>
        </div>
      )
    },
  },

  {
    id: 'rendez-vous-details',
    header: 'Rendez-Vous',
    cell: ({ row }) => {
      const rendezVous = row.original
      const { user } = useUser()
      if (!rendezVous.id) {
        return (
          <Button disabled size="sm">
            Voir Rendez-Vous
          </Button>
        )
      }

      return (
        <Link
          to={
            user!.role === 'ROLE_LOCATAIRE'
              ? '/locataire/rendez-vous/$rendezVousId'
              : '/proprietaire/rendez-vous/$rendezVousId'
          }
          params={{ rendezVousId: rendezVous.id.toString() }}
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
      const rendezVous = row.original
      const [open, setOpen] = useState(false)
      const [triggered, setTriggered] = useState('')
      const { user } = useUser()

      // Hooks pour les mutations API
      const rendezVousUpdate = api.rendezVous.update()
      const rendezVousDelete = api.rendezVous.delete()

      function closeDialog() {
        setOpen(false)
      }

      const deleteRendezVous = () => {
        if (!rendezVous.id) {
          toast.error('Erreur !!!', {
            description: 'Rendez-vous non défini',
          })
          return
        }

        rendezVousDelete.mutate(rendezVous.id, {
          onSettled() {
            closeDialog()
            publish('refresh_rendezvous_table', {})
          },
          onSuccess() {
            toast.success('Suppression effectuée', {
              description: 'La suppression du rendez-vous est un succès',
            })
          },
          onError() {
            toast.error('Erreur !!!', {
              description: "Une erreur s'est produite lors de la suppression",
            })
          },
        })
      }

      const updateStatut = (nouveauStatut: RendezVousModel['statut']) => {
        if (!rendezVous.id) {
          toast.error('Erreur !!!', {
            description: 'Rendez-vous non défini',
          })
          return
        }

        const formData = new FormData()
        formData.append('statut', nouveauStatut)

        rendezVousUpdate.mutate(
          { data: formData, id: rendezVous.id },
          {
            onSettled() {
              closeDialog()
              publish('refresh_proprietaire_rendez_vous_table', {})
            },
            onSuccess() {
              toast.success('Modification effectuée', {
                description: 'Le statut du rendez-vous a été mis à jour',
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
                rendezVous.statut === 'EN_ATTENTE' && (
                  <DropdownMenuItem
                    className="flex flex-row px-2 justify-between text-green-600"
                    onClick={() => updateStatut('CONFIRME')}
                  >
                    <span>Confirmer</span>
                    <SquarePen />
                  </DropdownMenuItem>
                )}

              {rendezVous.statut !== 'ANNULE' && (
                <DropdownMenuItem
                  className="flex flex-row px-2 justify-between text-orange-600"
                  onClick={() => updateStatut('ANNULE')}
                >
                  <span>Annuler</span>
                  <SquarePen />
                </DropdownMenuItem>
              )}

              {/* { user!.role==='ROLE_PROPRIETAIRE' && (<DialogTrigger asChild>
                <DropdownMenuItem
                  className="flex flex-row px-2 justify-between text-red-500"
                  onClick={() => setTriggered('delete')}
                >
                  Supprimer
                  <Trash />
                </DropdownMenuItem>
              </DialogTrigger>)} */}
            </DropdownMenuContent>
          </DropdownMenu>

          <DialogContent>
            {/* {triggered === 'delete' && (
              <>
                <DialogHeader>
                  <DialogTitle>Suppression Rendez-vous</DialogTitle>
                  <DialogDescription>
                    Êtes-vous sûr de vouloir supprimer ce rendez-vous pour la chambre "{rendezVous.chambre.titre}" ?
                  </DialogDescription>
                </DialogHeader>
                <div className="py-4">
                  <div className="text-sm text-gray-600">
                    <p><strong>Locataire:</strong> #{rendezVous.locataireId}</p>
                    <p><strong>Date:</strong> {formatDateOnly(rendezVous.dateHeure)}</p>
                    <p><strong>Heure:</strong> {formatTimeOnly(rendezVous.dateHeure)}</p>
                    <p><strong>Statut:</strong> {rendezVous.statut}</p>
                  </div>
                </div>
                <DialogFooter>
                  <div className="flex flex-row w-full gap-4">
                    <Button
                      className="w-1/3"
                      variant="destructive"
                      onClick={deleteRendezVous}
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
            )} */}
          </DialogContent>
        </Dialog>
      )
    },
  },
]
