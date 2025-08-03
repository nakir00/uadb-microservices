import { keepPreviousData, useMutation, useQuery } from '@tanstack/react-query'
import { instance } from '../api'
import type { PageableResponse } from '../api'
import type { ChambreModel } from './chambre'

export type RendezVousModel = {
  id?: number
  locataireId: 0
  dateHeure: string
  statut: 'EN_ATTENTE' | 'CONFIRME' | 'ANNULE'
  creeLe: string
  chambre: ChambreModel
}

type keys = keyof RendezVousModel
export const rendezVousQueries = {
  // ---------------------------------------chambre
  getAll: (values: {
    page: number
    size: number
    sort?: Array<[keys, 'asc' | 'desc' | undefined, 'ignorecase' | undefined]>
    locataireId?: number
    dateHeureMin?: string
    dateHeureMax?: string
    statut?: 'EN_ATTENTE' | 'CONFIRME' | 'ANNULE'
    creeLeSince?: string
    creeLeUntil?: string
    chambreId?: number
    chambreTitre?: string
    chambreDescription?: string
    chambreTaille?: string
    chambreType?: 'SIMPLE' | 'APPARTEMENT' | 'MAISON'
    chambreMeublee?: boolean
    chambreSalleDeBain?: boolean
    chambreDisponible?: boolean
    maisonId?: number
    maisonNom?: string
    maisonVille?: string
    maisonQuartier?: string
    proprietaireId?: number
    hasDateHeure?: boolean
    recentDays?: number
  }) =>
    useQuery<PageableResponse<ChambreModel>, Error>({
      queryKey: ['get-all-rendez-vous', Object.values(values)],
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

        return instance.get(`/rendez-vous?${searchParams.toString()}`)
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
      queryKey: ['get-one-rendez-vous', [id]],
      queryFn: () => instance.get(`rendez-vous/${id}`.trim()),
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
        locataireId,
        chambreId,
        dateHeure,
      }: {
        locataireId: number
        chambreId: number
        dateHeure: Date
      }) =>
        instance.post(`rendez-vous`, {
          locataireId,
          chambreId,
          dateHeure,
        }),
    }),

  update: () =>
    useMutation({
      mutationFn: ({ id, data }: { id: number; data: FormData }) =>
        instance.put(`rendez-vous/${id}`, data),
    }),

  delete: () =>
    useMutation({
      mutationFn: (id: number) => instance.delete(`proprietaire/chambre/${id}`),
    }),
}
