import { z } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { PhoneInput } from '@/components/ui/phone-number-input'

const LoginSchema = z.object({
  password: z
    .string()
    .min(6, { message: 'le mot de passe doit avoir au moins 6 caracteres' }),
  email: z
    .string()
    .min(2, "c'est trop court")
    .email('This is not a valid email.'),
})

export type LoginFormType = z.infer<typeof LoginSchema>

export function LoginForm({
  email = '',
  password = '',
  onSoumis,
}: {
  email?: string
  password?: string
  onSoumis: ({ email, password }: LoginFormType) => void
}) {
  // 1. Define your form.
  const form = useForm<z.infer<typeof LoginSchema>>({
    resolver: zodResolver(LoginSchema),
    defaultValues: {
      email: email,
      password: password,
    },
  })

  function onSubmit(values: z.infer<typeof LoginSchema>) {
    onSoumis(values)
  }

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="space-y-4 flex flex-col"
      >
        <FormField
          control={form.control}
          name="email"
          render={({ field }) => (
            <FormItem>
              <FormLabel className="text-center text-Nexa text-noir font-[800] ">
                adresse email
              </FormLabel>
              <FormControl>
                <Input
                  placeholder="ex:mame-aboulaye.wade@galsen.sn"
                  {...field}
                />
              </FormControl>
              <FormDescription>
                veuillez renseigner votre adresse email
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="password"
          render={({ field }) => (
            <FormItem>
              <FormLabel className="text-center text-Nexa text-noir font-[800] ">
                mot de passe
              </FormLabel>
              <FormControl>
                <Input type="password" {...field} />
              </FormControl>
              <FormDescription>
                veuillez renseigner votre mot de passe
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        <Button type="submit">S'inscrire</Button>
      </form>
    </Form>
  )
}
