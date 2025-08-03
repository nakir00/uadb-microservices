import { createFileRoute, redirect } from '@tanstack/react-router'
import { useEffect, useState } from 'react'
import { z } from 'zod'
import {
  AlertCircle,
  AlertTriangle,
  Calendar,
  CheckCircle2,
  Clock,
  CreditCard,
  Euro,
  FileText,
  FileX,
  Home,
  Mail,
  MapPin,
  Phone,
  User,
  XCircle,
} from 'lucide-react'
import { toast } from 'sonner'
import type { ContratModel } from '@/api/queries/contrat'
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from '@/components/ui/alert-dialog'
import { api } from '@/api/api'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Separator } from '@/components/ui/separator'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import { useUser } from '@/hooks/user'
import { NormalDataTable } from '@/blocs/proprietaire/table/tables/datatable'
import CreateProblemeButton from '@/components/locataire/contrat/probleme/create-probleme-button'
import { ProblemeCard } from '@/blocs/probleme-card'

const routeSchema = z.object({
  contratId: z.string(),
})

export const Route = createFileRoute(
  '/proprietaire/_proprietaireLayout/contrats/$contratId/',
)({
  parseParams: routeSchema.parse,
  component: RouteComponent,
  loader({ context }) {
    if (!context.auth.user) {
      throw redirect({ to: '/auth/login', from: '/proprietaire/contrats' })
    }
    return { proprietaireId: context.auth.user.id }
  },
})

function RouteComponent() {
  const { contratId } = Route.useParams()
  const { user } = useUser()

  const {
    isPending,
    data: contratData,
    refetch,
  } = api.contrat.getOne(parseInt(contratId))

  const resilierContrat = api.contrat.update()

  function handleResilierContrat() {
    const formData = new FormData()
    formData.append('statut', 'RESILIE')

    resilierContrat.mutate(
      {
        id: Number(contratId),
        data: formData,
      },
      {
        onSettled(data, error, variables, context) {},
        async onSuccess(data, variables, context) {
          toast('Modification effectuée', {
            description: 'La modification du rendez-vous est un succès',
          })
          refetch()
        },
        onError(error, variables, context) {
          toast.error('Erreur !!!', {
            description: " Une erreur s'est produite lors de la modification",
          })
        },
      },
    )
  }

  // Fonctions utilitaires
  const formatDate = (dateString: string): string => {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
    })
  }

  const formatDateTime = (dateString: string): string => {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  }

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

  const getStatutBadge = (statut: string) => {
    const variants = {
      ACTIF: { color: 'bg-green-100 text-green-800', text: 'Actif' },
      RESILIE: { color: 'bg-red-100 text-red-800', text: 'Résilié' },
    }
    const config = variants[statut as keyof typeof variants]
    return <Badge className={config.color}>{config.text}</Badge>
  }

  const getPaiementStatutIcon = (statut: string) => {
    return statut === 'PAYE' ? (
      <CheckCircle2 className="w-4 h-4 text-green-600" />
    ) : (
      <XCircle className="w-4 h-4 text-red-600" />
    )
  }

  const getPeriodiciteLabel = (periodicite: string) => {
    const labels = {
      JOURNALIER: 'Journalier',
      HEBDOMADAIRE: 'Hebdomadaire',
      MENSUEL: 'Mensuel',
    }
    return labels[periodicite as keyof typeof labels]
  }

  const getModePaiementLabel = (modePaiement: string) => {
    const labels = {
      VIREMENT: 'Virement bancaire',
      CASH: 'Espèces',
      MOBILEMONEY: 'Mobile Money',
    }
    return labels[modePaiement as keyof typeof labels]
  }

  if (isPending) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
          <p className="mt-2 text-muted-foreground">Chargement du contrat...</p>
        </div>
      </div>
    )
  }

  if (!contratData) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="text-center">
          <AlertCircle className="w-8 h-8 text-muted-foreground mx-auto mb-2" />
          <p className="text-muted-foreground">Contrat non trouvé</p>
        </div>
      </div>
    )
  }

  const contrat = contratData.data as ContratModel

  const paiementStats = {
    total: contrat.paiements.length,
    payes: contrat.paiements.filter((p) => p.statut === 'PAYE').length,
    montantTotal: contrat.paiements.reduce((sum, p) => sum + p.montant, 0),
    montantPaye: contrat.paiements
      .filter((p) => p.statut === 'PAYE')
      .reduce((sum, p) => sum + p.montant, 0),
  }

  const isContratActif = contrat.statut === 'ACTIF'
  const isContratExpire = new Date(contrat.dateFin).getMilliseconds() < new Date().getMilliseconds()

  return (
    <div className="flex flex-col gap-6 py-6">
      {/* En-tête */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <h1 className="text-3xl font-bold tracking-tight">
            Contrat #{contrat.id}
          </h1>
          <p className="text-muted-foreground">
            Détails du contrat de location
          </p>
        </div>
        <div className="flex items-center gap-2">
          {getStatutBadge(contrat.statut)}
          <Badge variant="outline">
            {getPeriodiciteLabel(contrat.periodicite)}
          </Badge>
        </div>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        {/* Colonne principale */}
        <div className="lg:col-span-2 space-y-6">
          {/* Informations générales */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <FileText className="w-5 h-5" />
                Informations générales
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-sm font-medium text-muted-foreground">
                    Date de début
                  </p>
                  <p className="flex items-center gap-2 mt-1">
                    <Calendar className="w-4 h-4" />
                    {formatDate(contrat.dateDebut)}
                  </p>
                </div>
                <div>
                  <p className="text-sm font-medium text-muted-foreground">
                    Date de fin 
                  </p>
                  <p className="flex items-center gap-2 mt-1">
                    <Calendar className="w-4 h-4" />
                    {formatDate(contrat.dateFin)}
                  </p>
                </div>
                <div>
                  <p className="text-sm font-medium text-muted-foreground">
                    Durée
                  </p>
                  <p className="flex items-center gap-2 mt-1">
                    <Clock className="w-4 h-4" />
                    {calculateDuration(contrat.dateDebut, contrat.dateFin)}
                  </p>
                </div>
                <div>
                  <p className="text-sm font-medium text-muted-foreground">
                    Créé le
                  </p>
                  <p className="text-sm mt-1">
                    {formatDateTime(contrat.creeLe)}
                  </p>
                </div>
              </div>

              <Separator />

              <div>
                <p className="text-sm font-medium text-muted-foreground mb-2">
                  Description
                </p>
                <p className="text-sm leading-relaxed">{contrat.description}</p>
              </div>
            </CardContent>
          </Card>

          {/* Informations financières */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Euro className="w-5 h-5" />
                Informations financières
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-2 lg:grid-cols-4 gap-6">
                <div className="text-center p-4 bg-muted/50 rounded-lg">
                  <p className="text-2xl font-bold text-primary">
                    {contrat.montantCaution.toLocaleString('fr-FR')}
                  </p>
                  <p className="text-sm text-muted-foreground">FCFA</p>
                  <p className="text-xs font-medium mt-1">Montant caution</p>
                </div>
                <div className="text-center p-4 bg-muted/50 rounded-lg">
                  <p className="text-2xl font-bold text-primary">
                    {contrat.moisCaution}
                  </p>
                  <p className="text-sm text-muted-foreground">mois</p>
                  <p className="text-xs font-medium mt-1">Période caution</p>
                </div>
                <div className="text-center p-4 bg-muted/50 rounded-lg">
                  <p className="text-lg font-bold">
                    {getPeriodiciteLabel(contrat.periodicite)}
                  </p>
                  <p className="text-xs font-medium mt-1">Périodicité</p>
                </div>
                <div className="text-center p-4 bg-muted/50 rounded-lg">
                  <p className="text-lg font-bold">
                    {getModePaiementLabel(contrat.modePaiement)}
                  </p>
                  <p className="text-xs font-medium mt-1">Mode paiement</p>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Onglets pour paiements et problèmes */}
          <Tabs defaultValue="paiements" className="w-full">
            <TabsList className="grid w-full grid-cols-2">
              <TabsTrigger value="paiements">
                Paiements ({contrat.paiements.length})
              </TabsTrigger>
              <TabsTrigger value="problemes">
                Problèmes ({contrat.problemes.length})
              </TabsTrigger>
            </TabsList>

            <TabsContent value="paiements" className="space-y-4">
              <Card>
                <CardHeader>
                  <CardTitle>Statistiques des paiements</CardTitle>
                  <CardDescription>
                    {paiementStats.payes}/{paiementStats.total} paiements
                    effectués
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="grid grid-cols-3 gap-4 mb-4">
                    <div className="text-center">
                      <p className="text-2xl font-bold text-green-600">
                        {paiementStats.montantPaye.toLocaleString('fr-FR')}
                      </p>
                      <p className="text-sm text-muted-foreground">
                        FCFA payés
                      </p>
                    </div>
                    <div className="text-center">
                      <p className="text-2xl font-bold text-red-600">
                        {(
                          paiementStats.montantTotal - paiementStats.montantPaye
                        ).toLocaleString('fr-FR')}
                      </p>
                      <p className="text-sm text-muted-foreground">
                        FCFA restants
                      </p>
                    </div>
                    <div className="text-center">
                      <p className="text-2xl font-bold">
                        {Math.round(
                          (paiementStats.payes / paiementStats.total) * 100,
                        )}
                        %
                      </p>
                      <p className="text-sm text-muted-foreground">
                        Progression
                      </p>
                    </div>
                  </div>

                  <div className="space-y-2">
                    {contrat.paiements.map((paiement) => (
                      <div
                        key={paiement.id}
                        className="flex items-center justify-between p-3 border rounded-lg"
                      >
                        <div className="flex items-center gap-3">
                          {getPaiementStatutIcon(paiement.statut)}
                          <div>
                            <p className="font-medium">
                              {paiement.montant.toLocaleString('fr-FR')} FCFA
                            </p>
                            <p className="text-sm text-muted-foreground">
                              Échéance: {formatDate(paiement.dateEcheance)}
                            </p>
                          </div>
                        </div>
                        <div className="text-right">
                          <Badge
                            variant={
                              paiement.statut === 'PAYE'
                                ? 'default'
                                : 'secondary'
                            }
                          >
                            {paiement.statut === 'PAYE' ? 'Payé' : 'Impayé'}
                          </Badge>
                          {paiement.datePaiement && (
                            <p className="text-xs text-muted-foreground mt-1">
                              Payé le {formatDate(paiement.datePaiement)}
                            </p>
                          )}
                        </div>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </TabsContent>

            <TabsContent value="problemes" className="space-y-4">
              <Card>
                <CardHeader>
                  <CardTitle>Problèmes signalés</CardTitle>
                  <CardDescription className="flex flex-row justify-between">
                    <span>
                      {contrat.problemes.length === 0
                        ? 'Aucun problème signalé pour ce contrat'
                        : `${contrat.problemes.length} problème(s) signalé(s)`}
                    </span>
                    <CreateProblemeButton
                      contratId={contrat.id!}
                      onCreate={() => {
                        refetch()
                      }}
                    />
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  {contrat.problemes.length === 0 ? (
                    <div className="text-center py-8">
                      <CheckCircle2 className="w-12 h-12 text-green-500 mx-auto mb-2" />
                      <p className="text-muted-foreground">Tout va bien !</p>
                      <CreateProblemeButton
                        contratId={contrat.id!}
                        onCreate={() => {
                          refetch()
                        }}
                      />
                    </div>
                  ) : (
                    <div className="space-y-4">
                      {contrat.problemes.map((probleme, index) => (
                        <ProblemeCard
                          key={probleme.id || Math.random()}
                          probleme={probleme}
                          onMarquerResolu={() => {
                            refetch()
                          }}
                        />
                        /*  */
                      ))}
                    </div>
                  )}
                </CardContent>
              </Card>
            </TabsContent>
          </Tabs>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Informations locataire */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <User className="w-5 h-5" />
                Locataire
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center gap-3">
                <Avatar>
                  <AvatarFallback>
                    {contrat.locataire.nomUtilisateur
                      .split(' ')
                      .map((n) => n[0])
                      .join('')}
                  </AvatarFallback>
                </Avatar>
                <div>
                  <p className="font-medium">
                    {contrat.locataire.nomUtilisateur}
                  </p>
                  <p className="text-sm text-muted-foreground">
                    #{contrat.locataire.id}
                  </p>
                </div>
              </div>

              <Separator />

              <div className="space-y-2">
                <div className="flex items-center gap-2">
                  <Mail className="w-4 h-4 text-muted-foreground" />
                  <span className="text-sm">{contrat.locataire.email}</span>
                </div>
                <div className="flex items-center gap-2">
                  <Phone className="w-4 h-4 text-muted-foreground" />
                  <span className="text-sm">{contrat.locataire.telephone}</span>
                </div>
                <div className="flex items-center gap-2">
                  <CreditCard className="w-4 h-4 text-muted-foreground" />
                  <span className="text-sm">CNI: {contrat.locataire.CNI}</span>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Informations chambre */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Home className="w-5 h-5" />
                Chambre
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div>
                <h4 className="font-medium">{contrat.chambre.titre}</h4>
                <p className="text-sm text-muted-foreground mt-1">
                  {contrat.chambre.description}
                </p>
              </div>

              <Separator />

              <div className="space-y-2">
                <div className="flex justify-between">
                  <span className="text-sm text-muted-foreground">Prix</span>
                  <span className="text-sm font-medium">
                    {contrat.chambre.prix.toLocaleString('fr-FR')} FCFA
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-muted-foreground">Taille</span>
                  <span className="text-sm font-medium">
                    {contrat.chambre.taille}m²
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-muted-foreground">Type</span>
                  <span className="text-sm font-medium">
                    {contrat.chambre.type}
                  </span>
                </div>
              </div>

              <Separator />

              <div>
                <p className="text-sm text-muted-foreground mb-2">Adresse</p>
                <div className="flex items-start gap-2">
                  <MapPin className="w-4 h-4 text-muted-foreground mt-0.5" />
                  <span className="text-sm">
                    {contrat.chambre.maison!.adresse}
                  </span>
                </div>
              </div>

              {/* Médias de la chambre */}
              {contrat.chambre.medias && contrat.chambre.medias.length > 0 && (
                <>
                  <Separator />
                  <div>
                    <p className="text-sm text-muted-foreground mb-2">Photos</p>
                    <div className="grid grid-cols-2 gap-2">
                      {contrat.chambre.medias.slice(0, 4).map((media) => (
                        <div
                          key={media.id}
                          className="aspect-square bg-muted rounded-lg overflow-hidden"
                        >
                          <img
                            src={media.url}
                            alt={media.description}
                            className="w-full h-full object-cover"
                          />
                        </div>
                      ))}
                    </div>
                  </div>
                </>
              )}
            </CardContent>
          </Card>
          <Card className="w-full border-red-200 bg-red-50">
            <CardHeader className="pb-4">
              <div className="flex items-center gap-3">
                <div className="p-2 bg-red-100 rounded-full">
                  <FileX className="w-5 h-5 text-red-600" />
                </div>
                <div>
                  <CardTitle className="text-lg text-red-800">
                    Résilier le contrat
                  </CardTitle>
                  <CardDescription className="text-red-600">
                    Action irréversible - Le contrat sera définitivement résilié
                  </CardDescription>
                </div>
              </div>
            </CardHeader>

            <CardContent className="space-y-4">
              

              {contrat.statut === 'ACTIF' && (
                <div className="bg-yellow-50 border border-yellow-200 p-3 rounded-lg">
                  <div className="flex items-center gap-2 text-yellow-800">
                    <AlertTriangle className="w-4 h-4" />
                    <span className="text-sm font-medium">
                      Ce contrat est déjà résilié ou inactif
                    </span>
                  </div>
                </div>
              )}

              {isContratExpire && isContratActif && (
                <div className="bg-orange-50 border border-orange-200 p-3 rounded-lg">
                  <div className="flex items-center gap-2 text-orange-800">
                    <AlertTriangle className="w-4 h-4" />
                    <span className="text-sm font-medium">
                      Ce contrat a déjà expiré selon sa date de fin
                    </span>
                  </div>
                </div>
              )}
            </CardContent>

            <CardFooter>
              <AlertDialog>
                <AlertDialogTrigger asChild>
                  <Button
                    variant="destructive"
                    className="w-full"
                    disabled={!isContratActif}
                  >
                    <FileX className="w-4 h-4 mr-2" />
                    Résilier le contrat
                  </Button>
                </AlertDialogTrigger>

                <AlertDialogContent>
                  <AlertDialogHeader>
                    <AlertDialogTitle className="flex items-center gap-2 text-red-600">
                      <AlertTriangle className="w-5 h-5" />
                      Confirmer la résiliation du contrat
                    </AlertDialogTitle>
                    <AlertDialogDescription className="space-y-3">
                      <p>
                        Êtes-vous sûr de vouloir résilier ce contrat ? Cette
                        action est <strong>irréversible</strong>.
                      </p>

                      <div className="bg-red-50 p-3 rounded-lg border border-red-200">
                        <h4 className="font-medium text-red-800 mb-2">
                          Conséquences de la résiliation :
                        </h4>
                        <ul className="text-sm text-red-700 space-y-1">
                          <li>• Le contrat sera marqué comme "RÉSILIÉ"</li>
                          <li>• Les paiements futurs seront annulés</li>
                          <li>
                            • La caution pourra être restituée selon les
                            conditions
                          </li>
                          <li>• Cette action ne peut pas être annulée</li>
                        </ul>
                      </div>

                      <div className="bg-gray-50 p-3 rounded-lg">
                        <p className="text-sm text-gray-600">
                          <strong>Contrat concerné :</strong> Du{' '}
                          {formatDate(contrat.dateDebut)} au{' '}
                          {formatDate(contrat.dateFin)}
                        </p>
                        <p className="text-sm text-gray-600">
                          <strong>Caution :</strong>{' '}
                          {contrat.montantCaution.toLocaleString()} FCFA
                        </p>
                      </div>
                    </AlertDialogDescription>
                  </AlertDialogHeader>

                  <AlertDialogFooter>
                    <AlertDialogCancel>Annuler</AlertDialogCancel>
                    <AlertDialogAction
                      onClick={() => handleResilierContrat()}
                      className="bg-red-600 hover:bg-red-700"
                    >
                      Confirmer la résiliation
                    </AlertDialogAction>
                  </AlertDialogFooter>
                </AlertDialogContent>
              </AlertDialog>
            </CardFooter>
          </Card>
        </div>
      </div>
    </div>
  )
}
