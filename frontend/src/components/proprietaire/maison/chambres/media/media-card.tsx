import { Send, Trash } from 'lucide-react'
import React, { useState } from 'react'
import { toast } from 'sonner'
import type { MediaModel } from '@/api/queries/media'
import { api } from '@/api/api'
import { Button } from '@/components/ui/button'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'

type Props = { media: MediaModel; chambreId?: number }

const MediaCard = ({ media, chambreId }: Props) => {
  const [description, setDescription] = useState(media.description || '')
  const updateMedia = api.media.update()
  const deleteMedia = api.media.delete()

  const handleUpdateMedia = () => {
    if (!chambreId || !media.id) {
      toast.error('Chambre ID or Media ID is not defined')
      return
    }
    updateMedia.mutate(
      {
        chambreId: Number(chambreId),
        mediaId: media.id,
        description,
      },
      {
        onSuccess: () => {
          toast.success('Mise à jour réussie')
        },
        onError: () => {
          toast.error('Erreur lors de la mise à jour')
        },
      },
    )
  }

  const handleDeleteMedia = () => {
    if (!chambreId || !media.id) {
      toast.error('Chambre ID or Media ID is not defined')
      return
    }
    deleteMedia.mutate(
      { chambreId: Number(chambreId), mediaId: media.id },
      {
        onSuccess: () => {
          toast.success('Media supprimé avec succès')
        },
        onError: () => {
          toast.error('Erreur lors de la suppression du media')
        },
      },
    )
  }

  return (
    <Card key={media.id}>
      <CardHeader className="w-full">
        <CardTitle
          className={'w-full  flex flex-row justify-between items-center'}
        >
          <span>Media Nº{media.id}</span>
          <Badge>{media.type === 'PHOTO' ? 'Photo' : 'Vidéo'}</Badge>
          <Button
            onClick={() => {
              handleDeleteMedia()
            }}
            className=""
            variant="destructive"
            size="sm"
          >
            <Trash className="w-4 h-4" />
          </Button>
        </CardTitle>
        <CardDescription className=" flex flex-row items-center gap-2 justify-between">
          <Input
            value={description}
            className="text-black"
            onChange={(e) => {
              setDescription(e.target.value)
            }}
            placeholder="Description de l'image"
          />
          <Button
            onClick={() => {
              handleUpdateMedia()
            }}
            className=""
            variant="outline"
            size="sm"
          >
            <Send className="w-4 h-4" />
          </Button>
        </CardDescription>
      </CardHeader>
      <CardContent>
        {media.type === 'VIDEO' ? (
          <video controls className="w-full h-auto">
            <source src={media.url} type="video/mp4" />
          </video>
        ) : (
          <img
            src={media.url}
            alt={media.description}
            className="w-full h-auto"
          />
        )}
      </CardContent>
    </Card>
  )
}

export default MediaCard
