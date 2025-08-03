import { Link, createFileRoute, useNavigate } from '@tanstack/react-router'
import { toast } from 'sonner'
import { z } from 'zod'
import type { RegisterFormType } from '@/blocs/auth/forms/register/register-form'
import {
  Card,
  CardAction,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { RegisterForm } from '@/blocs/auth/forms/register/register-form'
import { useUser } from '@/hooks/user'

const productSearchSchema = z.object({
  redirect: z.string().optional(),
  chambreId: z.number().positive().optional(),
})

export const Route = createFileRoute('/auth/_authLayout/register')({
  validateSearch: productSearchSchema,
  component: RouteComponent,
})

function RouteComponent() {
  const { register } = useUser()
  const navigate = useNavigate({ from: '/auth/register' })
  const { redirect, chambreId } = Route.useSearch()

  function onSubmit(data: RegisterFormType) {
    register(
      data,
      ({ user }) => {
        if (user.role == 'ROLE_LOCATAIRE') {
          if (redirect) {
            navigate({ to: redirect,  search:{ chambreId: chambreId } })
            return
          }
          navigate({ to: '/locataire' })
        } else {
          navigate({ to: '/proprietaire' })
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
        vous avez deja un compte ?{' '}
        <Link
          className="text-primary"
          to="/auth/login"
          search={{ redirect: redirect , chambreId: chambreId}}
        >
          connectez vous !
        </Link>{' '}
      </div>
      <Card>
        <CardHeader>
          <CardTitle>S'inscrire</CardTitle>
          <CardDescription>Card Description</CardDescription>
        </CardHeader>
        <CardContent>
          <RegisterForm onSoumis={onSubmit} />
        </CardContent>
      </Card>
    </div>
  )
}
