import { keepPreviousData, useMutation, useQuery } from '@tanstack/react-query'
import { instance } from '../api'
import type { PageableResponse } from '../api'

export type MediaModel = {
  id?: number
  description: string
  url: string
  type: 'PHOTO' | 'VIDEO' 
  creeLe: string
}

type keys = keyof MediaModel
export const mediaQueries = {
  // ---------------------------------------media

  update: () =>
      useMutation({
        mutationFn: ({ chambreId, mediaId, description }: { chambreId: number; mediaId:number; description: string }) =>
          instance.put(`proprietaire/chambre/${chambreId}/media/${mediaId}`, {description}),
      }),
  delete: () =>
      useMutation({
        mutationFn: ({ chambreId, mediaId }: { chambreId: number; mediaId:number; }) =>
          instance.delete(`proprietaire/chambre/${chambreId}/media/${mediaId}`),
      }),
}
