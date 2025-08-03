import { QueryClient } from '@tanstack/react-query'
import axios from 'axios'
import { userQueries } from './queries/user'
import { maisonQueries } from './queries/maison'
import { chambreQueries } from './queries/chambre';
import { mediaQueries } from './queries/media';
import { rendezVousQueries } from './queries/rendez-vous';
import { contratQueries } from './queries/contrat';
import { paiementQueries } from './queries/paiement';
import { problemeQueries } from './queries/probleme';

export interface PageableResponse<T> {
  content: Array<T>;
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      empty: boolean;
      unsorted: boolean;
      sorted: boolean;
    };
    offset: number;
    unpaged: boolean;
    paged: boolean;
  };
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    unsorted: boolean;
    sorted: boolean;
  };
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}

export const instance = axios.create({
  baseURL: 'http://localhost:8080/api/',
  withCredentials: true,
  timeout: 100000,
  headers: { 'Content-type': 'application/json' },
})

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      refetchOnMount: false,
      refetchOnReconnect: false,
      retry: false,
      staleTime:0, /* 5 * 60 * 1000, */
    },
  },
})

export const api = {
  user: userQueries,
  maison: maisonQueries,
  chambre: chambreQueries,
  media: mediaQueries,
  rendezVous: rendezVousQueries,
  contrat: contratQueries,
  paiement: paiementQueries,
  probleme: problemeQueries,
}
