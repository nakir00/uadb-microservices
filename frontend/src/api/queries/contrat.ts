import { keepPreviousData, useMutation, useQuery } from '@tanstack/react-query'
import { instance } from '../api'
import type { PageableResponse } from '../api'
import type { ChambreModel } from './chambre'
import type { PaiementModel } from './paiement'
import type { ProblemeModel } from './probleme'
import type { UserModel } from './user'

export type ContratModel = {
  id?: number
  locataireId: number
  chambreId: number
  dateDebut: string
  dateFin: string
  montantCaution: number
  moisCaution: number
  description: string
  modePaiement: 'VIREMENT' | 'CASH' | 'MOBILEMONEY'
  periodicite: 'JOURNALIER' | 'HEBDOMADAIRE' | 'MENSUEL'
  statut: 'ACTIF' | 'RESILIE'
  creeLe: string
  locataire: UserModel
  chambre: ChambreModel
  paiements: Array<PaiementModel>
  problemes: Array<ProblemeModel>
}

type keys = keyof ContratModel
export const contratQueries = {
  // ---------------------------------------chambre
  getAll: (values: {
    page: number
    size: number
    sort?: Array<[keys, 'asc' | 'desc' | undefined, 'ignorecase' | undefined]>
    id?: number
    locataireId?: number
    chambreId?: number
    dateDebut?: string
    dateDebutFrom?: string
    dateDebutTo?: string
    dateFin?: string
    dateFinFrom?: string
    dateFinTo?: string
    montantCaution?: number
    montantCautionMin?: number
    montantCautionMax?: number
    moisCaution?: number
    moisCautionMin?: number
    moisCautionMax?: number
    description?: string
    modePaiement?: 'VIREMENT' | 'CASH' | 'MOBILEMONEY'
    modesPaiement?: Array<'VIREMENT' | 'CASH' | 'MOBILEMONEY'>
    periodicite?: 'JOURNALIER' | 'HEBDOMADAIRE' | 'MENSUEL'
    periodicites?: Array<'JOURNALIER' | 'HEBDOMADAIRE' | 'MENSUEL'>
    statut?: 'ACTIF' | 'RESILIE'
    statuts?: Array<'ACTIF' | 'RESILIE'>
    creeLe?: string
    creeLeFrom?: string
    creeLeTo?: string
  }) =>
    useQuery<PageableResponse<ChambreModel>, Error>({
      queryKey: ['get-all-contrats', Object.values(values)],
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

        return instance.get(`/contrat?${searchParams.toString()}`)
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
      queryKey: ['get-one-contrat', [id]],
      queryFn: () => instance.get(`contrat/${id}`.trim()),
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
        dateDebut,
        dateFin,
        montantCaution,
        moisCaution,
        description,
        modePaiement,
        periodicite,
        statut,
      }: {
        locataireId: number
        chambreId: number
        dateDebut: string
        dateFin: string
        montantCaution: number
        moisCaution: number
        description: string
        modePaiement: 'VIREMENT' | 'CASH' | 'MOBILEMONEY'
        periodicite: 'JOURNALIER' | 'HEBDOMADAIRE' | 'MENSUEL'
        statut: 'ACTIF' | 'RESILIE'
      }) =>
        instance.post(`/contrat`, {
          locataireId,
          chambreId,
          dateDebut,
          dateFin,
          montantCaution,
          moisCaution,
          description,
          modePaiement,
          periodicite,
          statut,
        }),
    }),

  update: () =>
    useMutation({
      mutationFn: ({ id, data }: { id: number; data: FormData }) =>
        instance.put(`contrat/${id}`, data),
    }),

  delete: () =>
    useMutation({
      mutationFn: (id: number) => instance.delete(`contrat/${id}`),
    }),
}
