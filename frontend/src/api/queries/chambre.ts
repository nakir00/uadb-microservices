import { keepPreviousData, useMutation, useQuery } from '@tanstack/react-query'
import { instance } from '../api'
import type { PageableResponse } from '../api'
import type { MediaModel } from './media'
import type { MaisonModel } from './maison'

export type ChambreModel = {
  id?: number
  maisonId: number
  titre: string
  description: string
  taille: string
  type: 'SIMPLE' | 'APPARTEMENT' | 'MAISON'
  meublee: boolean
  salleDeBain: boolean
  disponible: boolean
  prix: number
  creeLe: string
  maison?: MaisonModel
  medias?: Array<MediaModel>
}

type keys = keyof ChambreModel
export const chambreQueries = {
  // ---------------------------------------chambre
  getAll: (values: {
    page: number
    size: number
    sort?: Array<[keys, 'asc' | 'desc' | undefined, 'ignorecase' | undefined]>
    titre?: string
    description?: string
    taille?: string
    type?: ChambreModel['type']
    meublee?: boolean
    salleDeBain?: boolean
    disponible?: boolean
    prixMin?: number
    prixMax?: number
    creeLeSince?: string
    creeLeUntil?: string
    proprietaireId?: number
    maisonId?: number
    maisonNom?: string
    maisonVille?: string
    maisonQuartier?: string
  }) =>
    useQuery<PageableResponse<ChambreModel>, Error>({
      queryKey: ['get-all-chambre', Object.values(values)],
      queryFn: () => {
        const searchParams = new URLSearchParams(values as any)
        Object.keys(values).forEach((key) => {
          if (values[key] === undefined) {
            delete values[key]
          }
        })
        if (values.sort) {
          searchParams.delete('sort')
          values.sort.forEach((s) => {
            searchParams.append('sort', s.join(','))
          })
        }

        return instance.get(`proprietaire/chambre?${searchParams.toString()}`)
      },
      staleTime: Number.POSITIVE_INFINITY,
      cacheTime: 0,
      enabled: true,
      options: {
        staleTime: 0,
        cacheTime: 0,
      },
    }),
  AsGuestGetAll: (values: {
    page: number
    size: number
    sort?: Array<[keys, 'asc' | 'desc' | undefined, 'ignorecase' | undefined]>
    titre?: string
    description?: string
    taille?: string
    type?: ChambreModel['type']
    meublee?: boolean
    salleDeBain?: boolean
    disponible?: boolean
    prixMin?: number
    prixMax?: number
    creeLeSince?: string
    creeLeUntil?: string
    proprietaireId?: number
    maisonId?: number
    maisonNom?: string
    maisonVille?: string
    maisonQuartier?: string
  }) =>
    useQuery<any, Error>({
      queryKey: ['get-all-chambre', Object.values(values)],
      queryFn: () => {
        const searchParams = new URLSearchParams(values as any)
        Object.keys(values).forEach((key) => {
          if (values[key] === undefined) {
            delete values[key]
          }
        })
        if (values.sort) {
          searchParams.delete('sort')
          values.sort.forEach((s) => {
            searchParams.append('sort', s.join(','))
          })
        }

        return instance.get<PageableResponse<ChambreModel>>(`guest/chambre?${searchParams.toString()}`)
      },
      staleTime: Number.POSITIVE_INFINITY,
      cacheTime: 0,
      enabled: true,
      options: {
        staleTime: 0,
        cacheTime: 0,
      },
    }),
  getOne: (id: number) =>
    useQuery({
      queryKey: ['get-one-proprietaire', [id]],
      queryFn: () => instance.get(`proprietaire/chambre/${id}`.trim()),
      staleTime: Number.POSITIVE_INFINITY,
      cacheTime: 0,
      enabled: true,
      options: {
        staleTime: 0,
        cacheTime: 0,
      },
    }),
  AsGuestgetOne: (id: number) =>
    useQuery<ChambreModel>({
      queryKey: ['get-one-guest', [id]],
      queryFn: () => instance.get(`guest/chambre/${id}`.trim()),
      staleTime: Number.POSITIVE_INFINITY,
      cacheTime: 0,
      enabled: true,
      options: {
        staleTime: 0,
        cacheTime: 0,
      },
    }),
  create: () =>
    useMutation({
      mutationFn: ({
        maisonId,
        titre,
        taille,
        description,
        type,
        meublee,
        salleDeBain,
        disponible,
        prix,
      }: {
        maisonId: number
        titre: string
        taille: string
        description: string
        type: 'SIMPLE' | 'APPARTEMENT' | 'MAISON'
        meublee: boolean
        salleDeBain: boolean
        disponible: boolean
        prix: number
      }) =>
        instance.post(`proprietaire/chambre`, {
          maisonId,
          titre,
          taille,
          description,
          type,
          meublee,
          salleDeBain,
          disponible,
          prix,
        }),
    }),
  update: () =>
    useMutation({
      mutationFn: ({ id, data }: { id: number; data: FormData }) =>
        instance.put(`proprietaire/chambre/${id}`, data),
    }),
  delete: () =>
    useMutation({
      mutationFn: (id: number) => instance.delete(`proprietaire/chambre/${id}`),
    }),

  createAttachMedia: () =>
    useMutation({
      mutationFn: ({ id, files }: { id: number; files: FormData }) =>
        instance.post(`proprietaire/chambre/${id}/media`, files, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        }),
    }),
}
