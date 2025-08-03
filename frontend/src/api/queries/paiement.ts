import { keepPreviousData, useMutation, useQuery } from '@tanstack/react-query'
import { instance } from '../api'
import type { PageableResponse } from '../api'
import type { ChambreModel } from './chambre'
import type { paiementModel } from './paiement'
import type { ContratModel } from './contrat'

export type PaiementModel = {
  id?: number
  paiementId: number
  montant: number
  statut: 'PAYE' | 'IMPAYE'
  dateEcheance: string
  datePaiement: string
  creeLe: string
  contrat: ContratModel
}

type keys = keyof PaiementModel
export const paiementQueries = {
  // ---------------------------------------chambre
  getAll: (values: {
    page: number
    size: number
    sort?: Array<[keys, 'asc' | 'desc' | undefined, 'ignorecase' | undefined]>
    id?: number
    paiementId?: number
    paiementIds?: Array<number>
    montant?: number
    montantMin?: number
    montantMax?: number
    statut?: 'PAYE' | 'IMPAYE'
    statuts?: Array<'PAYE' | 'IMPAYE'>
    dateEcheance?: string
    dateEcheanceFrom?: string
    dateEcheanceTo?: string
    datePaiement?: string
    datePaiementFrom?: string
    datePaiementTo?: string
    creeLe?: string
    creeLeFrom?: string
    creeLeTo?: string
    isPaye?: boolean
    isEnRetard?: boolean
    echeanceAvant?: string
    echeanceApres?: string
    enRetard?: boolean
    paye?: boolean
  }) =>
    useQuery<PageableResponse<ChambreModel>, Error>({
      queryKey: ['get-all-paiements', Object.values(values)],
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

        return instance.get(`/paiement?${searchParams.toString()}`)
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
      queryFn: () => instance.get(`paiement/${id}`.trim()),
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
        instance.post(`paiement`, {
          locataireId,
          chambreId,
          dateHeure,
        }),
    }),

  update: () =>
    useMutation({
      mutationFn: ({ id, data }: { id: number; data: FormData }) =>
        instance.put(`paiement/${id}`, data),
    }),

  delete: () =>
    useMutation({
      mutationFn: (id: number) => instance.delete(`paiement/${id}`),
    }),
}
