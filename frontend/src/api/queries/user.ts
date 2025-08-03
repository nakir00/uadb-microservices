import { useMutation } from '@tanstack/react-query'
import { instance } from '../api'

export type UserModel = {
  id?: number
  nomUtilisateur: string
  email: string
  telephone: string
  CNI: string
  role: "ROLE_LOCATAIRE" | "ROLE_PROPRIETAIRE"
  creeLe: string
}

export const userQueries = {
  // ---------------------------------------auth
  register: () =>
    useMutation({
      mutationFn: (utilisateur: Omit<UserModel, 'id' | 'creeLe'> & { password: string }) =>
        instance.post(`auth/register`, utilisateur),
    }),
  login: () =>
    useMutation({
      mutationFn: ({ email, password }: { email: string; password: string }) =>
        instance.post(`auth/login`, { email, password }),
    }),
  // a implementer coté back
  logout: () => useMutation({ mutationFn: () => instance.get(`auth/logout`) }),
}
