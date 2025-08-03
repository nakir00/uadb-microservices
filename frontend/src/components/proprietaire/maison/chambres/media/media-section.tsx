import React from 'react'
import { toast } from 'sonner'
import MediaCard from './media-card'
import type { FileMetadata } from '@/hooks/use-file-upload'
import type { ChambreModel } from '@/api/queries/chambre'
import type { MediaModel } from '@/api/queries/media'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import FileUpload from '@/components/ui/originUi/file-upload'
import { api } from '@/api/api'

type Props = {
    chambreId: number
}

const MediaSection = ({ chambreId }: Props) => {

    const chambreCreateAttachMedia = api.chambre.createAttachMedia()
    const {data: chambre , refetch, isLoading} = api.chambre.getOne(chambreId)

    if(!isLoading){
        console.log(chambre);
        
    }
    const handleFileChange = (files: Array<File>) => {
        if (!chambreId) {
          toast.error('Chambre ID is not defined')
          return
        }
        const formData = new FormData()
        files.forEach((file) => {
          formData.append('files', file)
        })
        chambreCreateAttachMedia.mutate(
          {
            id: Number(chambreId),
            files: formData,
          },
          {
            onSettled(data, error, variables, context) {},
            async onSuccess(data, variables, context) {
              toast('Upload effectué', {
                description: "L'upload des images est un succés",
              })
              refetch()
            },
            onError(error, variables, context) {
              toast.error('Erreur !!!', {
                description: " Une erreur s'est produite lors de l'upload",
              })
            },
          },
        )
      }
  return (
    <div className="grid gap-4 md:grid-cols-[1fr_250px] lg:grid-cols-3 lg:gap-8">
      <div className="grid items-start gap-4 auto-rows-max lg:col-span-2 lg:gap-8">
        <Card>
          <CardHeader>
            <CardTitle>Formulaire d'image</CardTitle>
            <CardDescription>
              pour ajouter des images à cette chambre
            </CardDescription>
          </CardHeader>
          <CardContent>
            <FileUpload
              onSubmit={handleFileChange}
              maxFiles={5}
              maxSizeMB={100}
            />
          </CardContent>
        </Card>
      </div>
      <div className="grid items-start gap-4 auto-rows-max lg:gap-8">
        {isLoading?<span>loading...</span>: (chambre.data.medias && chambre.data.medias.length > 0 )? (
          chambre.data.medias.map((media) => (
            <MediaCard media={media} chambreId={Number(chambreId)} />
          ))
        ) : (
          <Card>
            <CardHeader>
              <CardTitle>Aucune image</CardTitle>
              <CardDescription>Cette chambre n'a pas d'images</CardDescription>
            </CardHeader>
            <CardContent>
              <p>
                Vous pouvez ajouter des images en utilisant le formulaire
                ci-dessus.
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  )
}

export default MediaSection
