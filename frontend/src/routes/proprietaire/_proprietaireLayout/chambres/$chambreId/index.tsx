import { createFileRoute, redirect, useNavigate } from '@tanstack/react-router'
import { ChevronLeft, Send, Trash } from 'lucide-react'
import { toast } from 'sonner'
import { useState } from 'react'
import type { ChambreModel } from '@/api/queries/chambre'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { TooltipProvider } from '@/components/ui/tooltip'
import { api, instance } from '@/api/api'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { InfoChambreForm } from '@/blocs/proprietaire/forms/maison/info-chambre-form'
import FileUpload from '@/components/ui/originUi/file-upload'
import { Input } from '@/components/ui/input'
import MediaCard from '@/components/proprietaire/maison/chambres/media/media-card'
import MediaSection from '@/components/proprietaire/maison/chambres/media/media-section'
import { RendezVousSection } from '@/components/proprietaire/maison/chambres/rendez-vous/rendez-vous-section'

export const Route = createFileRoute(
  '/proprietaire/_proprietaireLayout/chambres/$chambreId/',
)({
  beforeLoad({ context }) {
    if (!context.auth.user) {
      throw redirect({ to: '/auth/login', from: '/proprietaire/chambres' })
    }
  },
  async loader({ params }) {
    return await instance.get<ChambreModel>(
      `proprietaire/chambre/${params.chambreId}`,
    )
  },
  component: RouteComponent,
})

function RouteComponent() {
  const chambreUpdate = api.chambre.update()
  const chambreCreateAttachMedia = api.chambre.createAttachMedia()
  const { chambreId } = Route.useParams()
  const navigate = useNavigate()
  const { data: chambreData } = Route.useLoaderData()

  function handleUpdateChambre(values: {
    titre: string
    taille: string
    description: string
    type: 'SIMPLE' | 'APPARTEMENT' | 'MAISON'
    meublee: boolean
    salleDeBain: boolean
    disponible: boolean
    prix: number
  }) {
    if (!chambreId) {
      toast.error('Chambre ID is not defined')
      return
    }

    const formData = new FormData()
    formData.append('titre', values.titre)
    formData.append('taille', values.taille.toString())
    formData.append('description', values.description)
    formData.append('type', values.type)
    formData.append('prix', values.prix.toString())
    formData.append('disponible', values.disponible ? 'true' : 'false')
    formData.append('meublee', values.meublee ? 'true' : 'false')
    formData.append('salleDeBain', values.salleDeBain ? 'true' : 'false')
    formData.append('id', chambreId.toString())

    chambreUpdate.mutate(
      {
        id: Number(chambreId),
        data: formData,
      },
      {
        onSettled(data, error, variables, context) {},
        async onSuccess(data, variables, context) {
          toast('Creation effectuée', {
            description: "La creation de l'ingredient est un succés",
          })
        },
        onError(error, variables, context) {
          toast.error('Erreur !!!', {
            description: " Une erreur s'est produite lors de la creation",
          })
        },
      },
    )
  }

  return (
    <>
      <div className="justify-center w-full gap-4 lg:gap-6 py-4 lg:py-6 ">
        <TooltipProvider>
          <div className="flex flex-col w-full min-h-screen">
            <div className="flex flex-col sm:gap-4 ">
              <div className="grid items-start flex-1 gap-4 sm:px-6 sm:py-0 md:gap-8">
                <Tabs defaultValue="update">
                  <div className="grid flex-1 gap-4 auto-rows-max">
                    <div className="flex items-center justify-between gap-4">
                      <div id="left" className="flex items-center gap-4">
                        <Button
                          onClick={() =>
                            navigate({ to: '/proprietaire/chambres' })
                          }
                          variant="outline"
                          size="icon"
                          className="h-7 w-7"
                        >
                          <ChevronLeft className="w-4 h-4" />
                          <span className="sr-only">retour</span>
                        </Button>
                        <h1 className="flex-1 text-xl font-semibold tracking-tight shrink-0 whitespace-nowrap sm:grow-0">
                          {chambreData.titre}
                        </h1>
                        <Badge variant="outline" className="ml-auto sm:ml-0">
                          chambre
                        </Badge>
                      </div>
                      <div id="center" className="flex items-center">
                        <TabsList>
                          <TabsTrigger value="update">Modification</TabsTrigger>
                          <TabsTrigger value="medias">Medias</TabsTrigger>
                          <TabsTrigger value="rendez-vous">
                            Rendez Vous
                          </TabsTrigger>
                          <TabsTrigger value="analytics">Analytics</TabsTrigger>
                        </TabsList>
                      </div>
                    </div>
                    <TabsContent value="update">
                      <div className="grid gap-4 md:grid-cols-[1fr_250px] lg:grid-cols-3 lg:gap-8">
                        <div className="grid items-start gap-4 auto-rows-max lg:col-span-2 lg:gap-8">
                          <Card>
                            <CardHeader>
                              <CardTitle>Formulaire</CardTitle>
                              <CardDescription>
                                pour modifier cette chambre
                              </CardDescription>
                            </CardHeader>
                            <CardContent>
                              <InfoChambreForm
                                onSoumis={handleUpdateChambre}
                                titre={chambreData.titre}
                                taille={chambreData.taille}
                                type={chambreData.type}
                                description={chambreData.description}
                                meublee={chambreData.meublee}
                                salleDeBain={chambreData.salleDeBain}
                                disponible={chambreData.disponible}
                                prix={chambreData.prix.toString()}
                              />
                            </CardContent>
                          </Card>

                          {/* <BoxTagsCard
                          tags={tagsAll}
                          box={box}
                          onSoumis={(e) => handleAttachTag(e)}
                        /> */}
                        </div>
                        {/* <div className="grid items-start gap-4 auto-rows-max lg:gap-8">
                          <FileUploadCard
                          image={box.image}
                          onFileChange={(e) => handleFileChange(e)}
                          />
                          <BoxCategoryCard
                            categories={categoriesAll}
                            defaultCategory={box.category ?? undefined}
                            onSoumis={(e) => updateBoxCategoryChange(e)}
                          /> 
                        
                       
                        </div> */}
                      </div>
                    </TabsContent>
                    {/* <TabsContent value="chambres">
                      <ChambresTable maisonId={Number(chambreId)} />
                    </TabsContent> */}
                    <TabsContent value="medias">
                      <MediaSection chambreId={chambreData.id??0} />
                    </TabsContent>
                    <TabsContent value="rendez-vous">
                      <RendezVousSection chambreId={Number(chambreId)} />
                    </TabsContent>
                    <TabsContent value="analytics">analytics</TabsContent>
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
