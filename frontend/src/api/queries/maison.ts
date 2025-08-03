import { keepPreviousData, useMutation, useQuery } from '@tanstack/react-query'
import { instance } from '../api'
import type { PageableResponse } from '../api'
import type { UserModel } from './user'

export type MaisonModel = {
  id?: number
  description: string
  adresse: string
  longitude: number
  latitude: number
  creeLe: string
  utilisateurDTO?: UserModel
}

type keys = keyof MaisonModel
export const maisonQueries = {
  // ---------------------------------------maison
  getAll: (values: {
    page: number
    size: number
    sort?: Array<[keys, 'asc' | 'desc' | undefined, 'ignorecase' | undefined]>
    proprietaireId?: number
    adresse?: string
    description?: string
    latMin?: number
    latMax?: number
    lonMin?: number
    lonMax?: number
    centerLat?: number
    centerLon?: number
    radiusKm?: number
    dateCreationMin?: string
    dateCreationMax?: string
    minChambres?: number
    maxChambres?: number
    hasChambresDisponibles?: boolean
    prixMinChambres?: number
    prixMaxChambres?: number
    typeChambre?: string
  }) =>
    useQuery<PageableResponse<MaisonModel>, Error>({
      queryKey: ['get-all-maisons', Object.values(values)],
      queryFn: () => {
        
        const searchParams = new URLSearchParams(values as any)
        if (values.sort) {
          searchParams.delete('sort')
          values.sort.forEach((s) => {
            searchParams.append('sort', s.join(','))
          })
        }

        return instance.get(
          `proprietaire/maison?${searchParams.toString()}`,
        )
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
    useQuery<MaisonModel>({
      queryKey: ['get-one', [id]],
      queryFn: () => instance.get(`proprietaire/maison/${id}`.trim()),
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
        description,
        adresse,
        longitude,
        latitude,
      }: {
        description: string
        adresse: string
        longitude: number
        latitude: number
      }) =>
        instance.post(`proprietaire/maison`, {
          description,
          adresse,
          longitude,
          latitude,
        }),
    }),
  update: () =>
    useMutation({
      mutationFn: ({ id, data }: { id: number; data: FormData }) =>
        instance.put(`proprietaire/maison/${id}`, data),
    }),
  delete: () =>
    useMutation({
      mutationFn: (id: number) => instance.delete(`proprietaire/maison/${id}`),
    }),
}
