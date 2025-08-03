import { createFileRoute, redirect, useNavigate } from '@tanstack/react-router'
import { Calendar, ChevronLeft, Clock, Home, MapPin, User } from 'lucide-react'
import { toast } from 'sonner'
import { useState } from 'react'
import type { RendezVousModel } from '@/api/queries/rendez-vous'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { TooltipProvider } from '@/components/ui/tooltip'
import { api } from '@/api/api'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Skeleton } from '@/components/ui/skeleton'
import { InfoRendezVousForm } from '@/blocs/locataire/forms/rendez-vous/info-rendez-vous-form'
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog'
import CreateContratButton from '@/components/proprietaire/rendez-vous/create-contrat-button'

export const Route = createFileRoute(
  '/proprietaire/_proprietaireLayout/rendez-vous/$rendezVousId/',
)({
  beforeLoad({ context }) {
    if (!context.auth.user) {
      throw redirect({ to: '/auth/login', from: '/proprietaire/rendez-vous' })
    }
  },
  component: RouteComponent,
})

function RouteComponent() {
  const rendezVousUpdate = api.rendezVous.update()
  const { rendezVousId } = Route.useParams()
  const navigate = useNavigate()
  // const { data } = Route.useLoaderData()
  const { data: rendezVousData, isLoading, refetch } = api.rendezVous.getOne(
    Number(rendezVousId),
  )

  function handleUpdateRendezVous(values: {
    time: Date
    statut?: 'EN_ATTENTE' | 'CONFIRME' | 'ANNULE'
  }) {
    if (!rendezVousId) {
      toast.error('Rendez-vous ID is not defined')
      return
    }

    const formData = new FormData()
    formData.append('dateHeure', values.time.toISOString())

    rendezVousUpdate.mutate(
      {
        id: Number(rendezVousId),
        data: formData,
      },
      {
        onSettled(data, error, variables, context) {},
        async onSuccess(data, variables, context) {
          toast('Modification effectuée', {
            description: 'La modification du rendez-vous est un succès',
          })
        },
        onError(error, variables, context) {
          toast.error('Erreur !!!', {
            description: " Une erreur s'est produite lors de la modification",
          })
        },
      },
    )
  }

  const getStatusBadgeVariant = (status: string) => {
    switch (status) {
      case 'CONFIRME':
        return 'default'
      case 'EN_ATTENTE':
        return 'secondary'
      case 'TERMINE':
        return 'outline'
      case 'ANNULE':
        return 'destructive'
      case 'REPORTE':
        return 'secondary'
      default:
        return 'outline'
    }
  }

  const getStatusLabel = (status: string) => {
    switch (status) {
      case 'EN_ATTENTE':
        return 'En attente'
      case 'CONFIRME':
        return 'Confirmé'
      case 'ANNULE':
        return 'Annulé'
      case 'REPORTE':
        return 'Reporté'
      case 'TERMINE':
        return 'Terminé'
      default:
        return status
    }
  }

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XOF',
      minimumFractionDigits: 0,
    }).format(price)
  }

  if (isLoading) {
    return (
      <div className="justify-center w-full gap-4 lg:gap-6 py-4 lg:py-6">
        <TooltipProvider>
          <div className="flex flex-col w-full min-h-screen">
            <div className="flex flex-col sm:gap-4">
              <div className="grid items-start flex-1 gap-4 sm:px-6 sm:py-0 md:gap-8">
                <Tabs defaultValue="details">
                  <div className="grid flex-1 gap-4 auto-rows-max">
                    <div className="flex items-center justify-between gap-4">
                      <div id="left" className="flex items-center gap-4">
                        <Button
                          onClick={() =>
                            navigate({ to: '/proprietaire/rendez-vous' })
                          }
                          variant="outline"
                          size="icon"
                          className="h-7 w-7"
                        >
                          <ChevronLeft className="w-4 h-4" />
                          <span className="sr-only">retour</span>
                        </Button>
                        <Skeleton className="h-7 w-48" />
                        <Skeleton className="h-6 w-20 ml-auto sm:ml-0" />
                      </div>
                      <div id="center" className="flex items-center">
                        <Skeleton className="h-10 w-80" />
                      </div>
                    </div>

                    <TabsContent value="details">
                      <div className="grid gap-4 md:grid-cols-2 lg:gap-8">
                        <Card>
                          <CardHeader>
                            <CardTitle className="flex items-center gap-2">
                              <Calendar className="w-5 h-5" />
                              Informations du rendez-vous
                            </CardTitle>
                          </CardHeader>
                          <CardContent className="space-y-4">
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Date et heure du rendez-vous
                              </p>
                              <Skeleton className="h-5 w-64 mt-1" />
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Statut
                              </p>
                              <Skeleton className="h-6 w-24 mt-1" />
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Créé le
                              </p>
                              <Skeleton className="h-5 w-48 mt-1" />
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                ID Locataire
                              </p>
                              <Skeleton className="h-5 w-20 mt-1" />
                            </div>
                          </CardContent>
                        </Card>

                        <Card>
                          <CardHeader>
                            <CardTitle className="flex items-center gap-2">
                              <Home className="w-5 h-5" />
                              Chambre concernée
                            </CardTitle>
                          </CardHeader>
                          <CardContent className="space-y-4">
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Titre
                              </p>
                              <Skeleton className="h-5 w-40 mt-1" />
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Type
                              </p>
                              <Skeleton className="h-5 w-32 mt-1" />
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Taille
                              </p>
                              <Skeleton className="h-5 w-28 mt-1" />
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Prix
                              </p>
                              <Skeleton className="h-6 w-36 mt-1" />
                            </div>
                            <div className="flex gap-2">
                              <Skeleton className="h-6 w-16" />
                              <Skeleton className="h-6 w-20" />
                              <Skeleton className="h-6 w-18" />
                            </div>
                          </CardContent>
                        </Card>
                      </div>
                    </TabsContent>
                  </div>
                </Tabs>
              </div>
            </div>
          </div>
        </TooltipProvider>
      </div>
    )
  }

  if (!rendezVousData) {
    return (
      <div className="justify-center w-full gap-4 lg:gap-6 py-4 lg:py-6">
        <TooltipProvider>
          <div className="flex flex-col w-full min-h-screen">
            <div className="flex flex-col sm:gap-4">
              <div className="grid items-start flex-1 gap-4 sm:px-6 sm:py-0 md:gap-8">
                <Card>
                  <CardContent className="flex flex-col items-center justify-center py-12">
                    <p className="text-lg font-medium text-muted-foreground">
                      Rendez-vous introuvable
                    </p>
                    <p className="text-sm text-muted-foreground mt-2">
                      Le rendez-vous demandé n'existe pas ou n'est plus
                      disponible.
                    </p>
                    <Button
                      onClick={() =>
                        navigate({ to: '/proprietaire/rendez-vous' })
                      }
                      variant="outline"
                      className="mt-4"
                    >
                      Retour à la liste
                    </Button>
                  </CardContent>
                </Card>
              </div>
            </div>
          </div>
        </TooltipProvider>
      </div>
    )
  }
  const rv = rendezVousData.data as RendezVousModel

  return (
    <>
      <div className="justify-center w-full gap-4 lg:gap-6 py-4 lg:py-6 ">
        <TooltipProvider>
          <div className="flex flex-col w-full min-h-screen">
            <div className="flex flex-col sm:gap-4 ">
              <div className="grid items-start flex-1 gap-4 sm:px-6 sm:py-0 md:gap-8">
                <Tabs defaultValue="details">
                  <div className="grid flex-1 gap-4 auto-rows-max">

                    <div className="flex items-center justify-between gap-4">
                      <div id="left" className="flex items-center gap-4">
                        <Button
                          onClick={() =>
                            navigate({ to: '/proprietaire/rendez-vous' })
                          }
                          variant="outline"
                          size="icon"
                          className="h-7 w-7"
                        >
                          <ChevronLeft className="w-4 h-4" />
                          <span className="sr-only">retour</span>
                        </Button>
                        <h1 className="flex-1 text-xl font-semibold tracking-tight shrink-0 whitespace-nowrap sm:grow-0">
                          Rendez-vous #{rv.id}
                        </h1>
                        <Badge
                          variant={getStatusBadgeVariant(rv.statut)}
                          className="ml-auto sm:ml-0"
                        >
                          {getStatusLabel(rv.statut)}
                        </Badge>
                      </div>
                      <div id="center" className="flex items-center">
                        <TabsList>
                          <TabsTrigger value="details">Détails</TabsTrigger>
                          <TabsTrigger value="modification">
                            Modification
                          </TabsTrigger>
                          <TabsTrigger value="chambre">Chambre</TabsTrigger>
                          {/* <TabsTrigger value="historique">
                            Historique
                          </TabsTrigger> */}
                        </TabsList>
                      </div>
                    </div>

                    <TabsContent value="details">
                      <div className="grid gap-4 md:grid-cols-2 lg:gap-8">
                        <Card>
                          <CardHeader>
                            <CardTitle className="flex items-center gap-2">
                              <Calendar className="w-5 h-5" />
                              Informations du rendez-vous
                            </CardTitle>
                          </CardHeader>
                          <CardContent className="space-y-4">
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Date et heure du rendez-vous
                              </p>
                              <p className="font-medium">
                                {new Date(rv.dateHeure).toLocaleDateString(
                                  'fr-FR',
                                  {
                                    weekday: 'long',
                                    year: 'numeric',
                                    month: 'long',
                                    day: 'numeric',
                                    hour: '2-digit',
                                    minute: '2-digit',
                                  },
                                )}
                              </p>
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Statut
                              </p>
                              <Badge variant={getStatusBadgeVariant(rv.statut)}>
                                {getStatusLabel(rv.statut)}
                              </Badge>
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Créé le
                              </p>
                              <p className="font-medium">
                                {new Date(rv.creeLe).toLocaleDateString(
                                  'fr-FR',
                                  {
                                    year: 'numeric',
                                    month: 'long',
                                    day: 'numeric',
                                    hour: '2-digit',
                                    minute: '2-digit',
                                  },
                                )}
                              </p>
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                ID Locataire
                              </p>
                              <p className="font-medium">#{rv.locataireId}</p>
                            </div>
                          </CardContent>
                        </Card>

                        <Card>
                          <CardHeader>
                            <CardTitle className="flex items-center gap-2">
                              <Home className="w-5 h-5" />
                              Chambre concernée
                            </CardTitle>
                          </CardHeader>
                          <CardContent className="space-y-4">
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Titre
                              </p>
                              <p className="font-medium">{rv.chambre.titre}</p>
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Type
                              </p>
                              <p className="font-medium">{rv.chambre.type}</p>
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Taille
                              </p>
                              <p className="font-medium">{rv.chambre.taille}</p>
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Prix
                              </p>
                              <p className="font-medium text-lg text-green-600">
                                {formatPrice(rv.chambre.prix)}
                              </p>
                            </div>
                            <div className="flex gap-2">
                              {rv.chambre.meublee && (
                                <Badge variant="outline">Meublée</Badge>
                              )}
                              {rv.chambre.salleDeBain && (
                                <Badge variant="outline">Salle de bain</Badge>
                              )}
                              {rv.chambre.disponible && (
                                <Badge variant="default">Disponible</Badge>
                              )}
                            </div>
                          </CardContent>
                        </Card>
                      </div>
                    </TabsContent>

                    <TabsContent value="modification">
                      <div className="grid gap-4 md:grid-cols-[1fr_250px] lg:grid-cols-3 lg:gap-8">
                        <div className="grid items-start gap-4 auto-rows-max lg:col-span-2 lg:gap-8">
                          <Card>
                            <CardHeader>
                              <CardTitle>Formulaire de modification</CardTitle>
                              <CardDescription>
                                Modifier les informations de ce rendez-vous
                              </CardDescription>
                            </CardHeader>
                            <CardContent>
                              <InfoRendezVousForm
                                onSoumis={handleUpdateRendezVous}
                                time={new Date(rv.dateHeure)}
                                // statut={rendezVousData.statut}
                              />
                            </CardContent>
                          </Card>
                        </div>
                        <div className="grid items-start gap-4 auto-rows-max lg:gap-8">
                          <Card>
                            <CardHeader>
                              <CardTitle>Actions rapides</CardTitle>
                            </CardHeader>
                            <CardContent className="space-y-2">
                              {rv.statut === 'EN_ATTENTE' && (
                                <Button
                                  variant="default"
                                  className="w-full justify-start"
                                  onClick={() => {
                                    handleUpdateRendezVous({
                                      time: new Date(rv.dateHeure),
                                      statut: 'CONFIRME',
                                    })
                                  }}
                                >
                                  Confirmer le rendez-vous
                                </Button>
                              )}

                              <Button
                                variant="destructive"
                                className="w-full justify-start"
                                onClick={() => {
                                  handleUpdateRendezVous({
                                    time: new Date(rv.dateHeure),
                                    statut: 'ANNULE',
                                  })
                                }}
                              >
                                Annuler le rendez-vous
                              </Button>
                            </CardContent>
                          </Card>
                          {rv.statut==="CONFIRME" && (
                            <CreateContratButton locataireId={rv.locataireId} chambreId={rv.chambre.id!} />
                          )}
                        </div>
                      </div>
                    </TabsContent>

                    <TabsContent value="chambre">
                      <div className="grid gap-4 md:grid-cols-2 lg:gap-8">
                        <Card>
                          <CardHeader>
                            <CardTitle className="flex items-center gap-2">
                              <Home className="w-5 h-5" />
                              Détails de la chambre
                            </CardTitle>
                          </CardHeader>
                          <CardContent className="space-y-4">
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Description
                              </p>
                              <p className="font-medium">
                                {rv.chambre.description}
                              </p>
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Créée le
                              </p>
                              <p className="font-medium">
                                {new Date(rv.chambre.creeLe).toLocaleDateString(
                                  'fr-FR',
                                )}
                              </p>
                            </div>
                            <Button
                              variant="outline"
                              onClick={() =>
                                navigate({
                                  to: `/proprietaire/chambres/${rv.chambre.id}`,
                                })
                              }
                            >
                              Voir la chambre complète
                            </Button>
                          </CardContent>
                        </Card>

                        <Card>
                          <CardHeader>
                            <CardTitle className="flex items-center gap-2">
                              <MapPin className="w-5 h-5" />
                              Propriété
                            </CardTitle>
                          </CardHeader>
                          <CardContent className="space-y-4">
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Adresse
                              </p>
                              <p className="font-medium">
                                {rv.chambre.maison!.adresse}
                              </p>
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Propriétaire
                              </p>
                              <p className="font-medium">
                                {
                                  rv.chambre.maison!.utilisateurDTO!
                                    .nomUtilisateur
                                }
                              </p>
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Contact
                              </p>
                              <p className="font-medium">
                                {rv.chambre.maison!.utilisateurDTO!.email}
                              </p>
                              <p className="font-medium">
                                {rv.chambre.maison!.utilisateurDTO!.telephone}
                              </p>
                            </div>
                            <div>
                              <p className="text-sm text-muted-foreground">
                                Description de la maison
                              </p>
                              <p className="font-medium">
                                {rv.chambre.maison!.description}
                              </p>
                            </div>
                          </CardContent>
                        </Card>
                      </div>

                      {rv.chambre.medias && rv.chambre.medias.length > 0 && (
                        <Card>
                          <CardHeader>
                            <CardTitle>Médias de la chambre</CardTitle>
                            <CardDescription>
                              {rv.chambre.medias.length || 0} média(s)
                              disponible(s)
                            </CardDescription>
                          </CardHeader>
                          <CardContent>
                            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                              {rv.chambre.medias.map((media) => (
                                <div key={media.id} className="relative group">
                                  {media.type === 'PHOTO' ? (
                                    <img
                                      src={media.url}
                                      alt={
                                        media.description ||
                                        'Photo de la chambre'
                                      }
                                      className="w-full h-32 object-cover rounded-lg"
                                    />
                                  ) : (
                                    <video controls className="w-full h-auto">
                                      <source
                                        src={media.url}
                                        type="video/mp4"
                                      />
                                    </video>
                                  )}
                                  {media.description && (
                                    <p className="text-xs text-center mt-1 text-muted-foreground">
                                      {media.description}
                                    </p>
                                  )}
                                </div>
                              ))}
                            </div>
                          </CardContent>
                        </Card>
                      )}
                    </TabsContent>
{/* 
                    <TabsContent value="historique">
                      <Card>
                        <CardHeader>
                          <CardTitle>Historique des modifications</CardTitle>
                          <CardDescription>
                            Suivi des changements apportés à ce rendez-vous
                          </CardDescription>
                        </CardHeader>
                        <CardContent>
                          <div className="space-y-4">
                            <div className="flex items-center gap-4 p-4 border rounded-lg">
                              <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
                              <div className="flex-1">
                                <p className="font-medium">Rendez-vous créé</p>
                                <p className="text-sm text-muted-foreground">
                                  {new Date(rv.creeLe).toLocaleDateString(
                                    'fr-FR',
                                    {
                                      year: 'numeric',
                                      month: 'long',
                                      day: 'numeric',
                                      hour: '2-digit',
                                      minute: '2-digit',
                                    },
                                  )}
                                </p>
                              </div>
                            </div>
                            <p className="text-muted-foreground text-center">
                              L'historique détaillé des modifications sera
                              affiché ici
                            </p>
                          </div>
                        </CardContent>
                      </Card>
                    </TabsContent> */}
                  </div>
                </Tabs>
              </div>
            </div>
          </div>
        </TooltipProvider>
      </div>
    </>
  )
}
