import {
  Link,
  createFileRoute,
  redirect,
  useNavigate,
} from '@tanstack/react-router'

import { toast } from 'sonner'
import { z } from 'zod'
import type { LoginFormType } from '@/blocs/auth/forms/login/login-form'
import {
  Card,
  CardAction,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { LoginForm } from '@/blocs/auth/forms/login/login-form'
import { useUser } from '@/hooks/user'

const productSearchSchema = z.object({
  redirect: z.string().optional(),
  chambreId: z.number().positive().optional()
})

export const Route = createFileRoute('/auth/_authLayout/login')({
  validateSearch: productSearchSchema,
  component: RouteComponent,
})

function RouteComponent() {
  const { login } = useUser()
  const navigate = useNavigate({ from: '/auth/login' })
  const { redirect, chambreId } = Route.useSearch()

  function onSubmit(data: LoginFormType) {
    login(
      data,
      async ({ user }) => {
        if (user.role == 'ROLE_LOCATAIRE') {
          if (redirect) {
            navigate({ to: redirect , search:{ chambreId: chambreId}})
            return
          }
          await navigate({ to: '/locataire' })
        } else {
          await navigate({ to: '/proprietaire' })
        }
      },
      (message) => {
        toast.error("une erreur s'est produite", {
          description: message,
        })
      },
    )
  }
  return (
    <div className=" flex flex-col justify-center items-center">
      <div className=" mb-5">
        {' '}
        pas de compte ?{' '}
        <Link
          className="text-primary"
          to="/auth/register"
          search={{ redirect: redirect, chambreId: chambreId }}
        >
          creez en un !
        </Link>{' '}
      </div>
      <Card>
        <CardHeader>
          <CardTitle>Se connecter</CardTitle>
          <CardDescription>
            connectez vous pour acceder a votre interface
          </CardDescription>
        </CardHeader>
        <CardContent>
          <LoginForm onSoumis={onSubmit} />
        </CardContent>
      </Card>
    </div>
  )
}
