import { createFileRoute, useNavigate, useParams } from '@tanstack/react-router'
import { ChevronLeft } from 'lucide-react'
import { toast } from 'sonner'
import type { MaisonModel } from '@/api/queries/maison'
import type { InfoMaisonFormType } from '@/blocs/proprietaire/forms/maison/info-maison-form'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { TooltipProvider } from '@/components/ui/tooltip'
import { InfoMaisonForm } from '@/blocs/proprietaire/forms/maison/info-maison-form'
import { api, instance } from '@/api/api'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import ChambresTable from '@/components/proprietaire/maison/chambres/chambre-table'

export const Route = createFileRoute(
  '/proprietaire/_proprietaireLayout/maisons/$maisonId/',
)({
  async loader({ params, context }) {
    return await instance.get<MaisonModel>(
      `proprietaire/maison/${params.maisonId}`,
    )
  },
  component: RouteComponent,
})

function RouteComponent() {

  const maisonCreate = api.maison.update()
  const { maisonId } = Route.useParams()
  const navigate = useNavigate()
  const { data: maisonData } = Route.useLoaderData()

  function handleAddMaisonType(values: InfoMaisonFormType) {
    if (!maisonId) {
      toast.error('Maison ID is not defined')
      return
    }
     const formData = new FormData()
      formData.append('adresse', values.adresse)
      formData.append('description', values.description)
      formData.append('longitude', values.lon)
      formData.append('latitude', values.lat)
      maisonCreate.mutate(
        {
          id: Number(maisonId),
          data: formData
        },
        {
          onSettled(data, error, variables, context) {
          },
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
                            navigate({ to: '/proprietaire/maisons' })
                          }
                          variant="outline"
                          size="icon"
                          className="h-7 w-7"
                        >
                          <ChevronLeft className="w-4 h-4" />
                          <span className="sr-only">retour</span>
                        </Button>
                        <h1 className="flex-1 text-xl font-semibold tracking-tight shrink-0 whitespace-nowrap sm:grow-0">
                          {maisonData.description}
                        </h1>
                        <Badge variant="outline" className="ml-auto sm:ml-0">
                          Maison
                        </Badge>
                      </div>
                      <div id="center" className="flex items-center">
                        <TabsList>
                          <TabsTrigger value="update">Modification</TabsTrigger>
                          <TabsTrigger value="chambres">Chambres</TabsTrigger>
                          <TabsTrigger value="analytics">Analytics</TabsTrigger>
                        </TabsList>
                      </div>
                      {/* <div id="right" className="flex items-center gap-4">
                      <Button
                        onClick={() => alert('faut activer ca nabyyyy !!!')}
                        variant="outline"
                        size="icon"
                        className="flex items-center w-auto p-4 h-7"
                      >
                        <ChevronLeft className="w-4 h-4 mr-2" />
                        <span>Activer</span>
                      </Button>
                    </div> */}
                    </div>
                    <TabsContent value="update">
                      <div className="grid gap-4 md:grid-cols-[1fr_250px] lg:grid-cols-3 lg:gap-8">
                        <div className="grid items-start gap-4 auto-rows-max lg:col-span-2 lg:gap-8">
                          <Card>
                            <CardHeader>
                              <CardTitle>Formulaire</CardTitle>
                              <CardDescription>
                                pour modifier cette maison
                              </CardDescription>
                            </CardHeader>
                            <CardContent>
                              <InfoMaisonForm
                                description={maisonData.description}
                                adresse={maisonData.adresse}
                                lat={maisonData.latitude}
                                lon={maisonData.longitude}
                                onSoumis={handleAddMaisonType}
                              />
                            </CardContent>
                          </Card>

                          {/* <BoxTagsCard
                          tags={tagsAll}
                          box={box}
                          onSoumis={(e) => handleAttachTag(e)}
                        /> */}
                        </div>
                        <div className="grid items-start gap-4 auto-rows-max lg:gap-8">
                          {/* <FileUploadCard
                          image={box.image}
                          onFileChange={(e) => handleFileChange(e)}
                        />
                        <BoxCategoryCard
                          categories={categoriesAll}
                          defaultCategory={box.category ?? undefined}
                          onSoumis={(e) => updateBoxCategoryChange(e)}
                        /> */}
                        </div>
                      </div>
                    </TabsContent>
                    <TabsContent value="chambres">
                      <ChambresTable maisonId={Number(maisonId)} />
                    </TabsContent>
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
