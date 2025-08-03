import { keepPreviousData, useMutation, useQuery } from '@tanstack/react-query'
import { instance } from '../api'
import type { PageableResponse } from '../api'
import type { ChambreModel } from './chambre'
import type { ContratModel } from './contrat'

export type ProblemeModel = {
  id?: number
  contratId: number
  signalePar: number
  description: string
  type: 'PLOMBERIE' | 'ELECTRICITE' | 'AUTRE'
  responsable: 'LOCATAIRE' | 'PROPRIETAIRE'
  resolu: boolean
  creeLe: string
  contrat: ContratModel
}

type keys = keyof ProblemeModel
export const problemeQueries = {
  // ---------------------------------------chambre
  getAll: (values: {
    page: number
    size: number
    sort?: Array<[keys, 'asc' | 'desc' | undefined, 'ignorecase' | undefined]>
    id?: number
    contratId: number
    contratIds: Array<number>
    signalePar: number
    signaleParList: Array<number>
    description: 'string'
    type: ProblemeModel['type']
    types: Array<ProblemeModel['type']>
    responsable: ProblemeModel['responsable']
    responsables: Array<ProblemeModel['responsable']>
    resolu: boolean
    creeLe: string
    creeLeFrom: string
    creeLeTo: string
    isEnCours: boolean
    isResolu: boolean
    nombreJoursDepuisCreation: number
    nombreJoursDepuisCreationMin: number
    nombreJoursDepuisCreationMax: number
    creeAvant: string
    creeApres: string
    enCours: boolean
  }) =>
    useQuery<PageableResponse<ChambreModel>, Error>({
      queryKey: ['get-all-problemess', Object.values(values)],
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

        return instance.get(`/probleme?${searchParams.toString()}`)
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
      queryKey: ['get-one-paiement', [id]],
      queryFn: () => instance.get(`probleme/${id}`.trim()),
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
        contratId,
        signalePar,
        description,
        type,
        responsable,
        resolu,
      }: {
        contratId: number
        signalePar: number
        description: string
        type: 'PLOMBERIE' | 'ELECTRICITE' | 'AUTRE'
        responsable: 'LOCATAIRE' | 'PROPRIETAIRE'
        resolu: boolean
      }) =>
        instance.post(`probleme`, {
          contratId,
          signalePar,
          description,
          type,
          responsable,
          resolu,
        }),
    }),

  update: () =>
    useMutation({
      mutationFn: ({ id, data }: { id: number; data: FormData }) =>
        instance.put(`probleme/${id}`, data),
    }),

  resoudre: () =>
    useMutation({
      mutationFn: ({ id }: { id: number }) =>
        instance.put(`probleme/${id}/resolu`),
    }),

  delete: () =>
    useMutation({
      mutationFn: (id: number) => instance.delete(`probleme/${id}`),
    }),
}
