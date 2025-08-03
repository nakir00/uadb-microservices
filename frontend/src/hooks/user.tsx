import { createContext, useContext } from 'react'
import { useLocalStorage } from './useLocalStorage'
import type { ReactNode } from 'react'
import type { UserModel } from '@/api/queries/user'
import { api } from '@/api/api'

export type UserContextType = {
  user: UserModel | undefined
  setUser: (user: UserModel | undefined) => void
  removeUser: () => void
}

const UserContext = createContext<UserContextType | undefined>(undefined)

export const UserContextProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useLocalStorage<UserModel | undefined>(
    'user',
    undefined,
  )

  const removeUser = () => localStorage.removeItem('user')

  return (
    <UserContext.Provider value={{ user: user, setUser: setUser, removeUser }}>
      {children}
    </UserContext.Provider>
  )
}

export const useUser = () => {
  const userData = useContext(UserContext)

  if (userData === undefined) {
    throw new Error('user data undefined')
  }

  const loginUser = api.user.login()
  const registerUser = api.user.register()
  const logoutUser = api.user.logout()

  const login = (
    {
      email,
      password,
    }: {
      email: string
      password: string
    },
    onSuccess?: (data: { user: UserModel }) => void,
    onError?: (message: string) => void,
  ) => {
    loginUser.mutate(
      {
        email,
        password,
      },
      {
        onSettled(data, error, variables, context) {},
        onSuccess(data, variables, context) {
          if (onSuccess) {
            
            userData.setUser( { ...data.data});

            onSuccess({user: {...data.data}})
          }
        },
        onError(error, variables, context) {
          const erreur: any = error as any
          if (onError) {
            onError(erreur.response.data.message)
          }
        },
      },
    )
  }

  const register = (
    {
      nomUtilisateur,
      email,
      password,
      telephone,
      CNI,
      role,
    }: Omit<UserModel, 'id' | 'creeLe'> & { password: string },
    onSuccess?: (data: { user: UserModel }) => void,
    onError?: (message: string) => void,
  ) => {
    registerUser.mutate(
      {
        nomUtilisateur,
        email,
        password,
        telephone,
        CNI,
        role,
      },
      {
        onSettled(data, error, variables, context) {},
        onSuccess(data, variables, context) {
          if (onSuccess) {
            userData.setUser(data.data);
            onSuccess({user :{...data.data}})

          }
        },
        onError(error, variables, context) {
          const erreur: any = error as any
          if (onError) {
            onError(erreur.response.data.message)
          }
        },
      },
    )
  }

  const logout = (
    onSuccess?: ( message: string ) => void,
    onError?: (message: string) => void,
  ) => {
    logoutUser.mutate(undefined, {
      onSettled(data, error, variables, context) {
        // router.reload()
      },
      onSuccess(data, variables, context) {
        if (onSuccess) {
          onSuccess(data.data)
          userData.setUser(undefined)
          userData.removeUser()
        }
      },
      onError(error, variables, context) {
        const erreur: any = error as any
        if (onError) {
          onError(erreur.response.data.message)
        }
      },
    })
  }

  

  return {
    user: userData.user,
    login,
    logout,
    register,
  }
}

export type useUserType = typeof useUser