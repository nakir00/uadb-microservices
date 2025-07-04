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

const RegisterSchema = z
  .object({
    nomUtilisateur: z.string().min(2, "c'est trop court"),
    password: z
      .string()
      .min(6, { message: 'le mot de passe doit avoir au moins 6 caracteres' }),
    confirmPassword: z.string(),
    email: z
      .string()
      .min(2, "c'est trop court")
      .email('This is not a valid email.'),
    telephone: z
      .string({ required_error: 'le numero est obligatoire' })
      .min(7, {
        message: 'le numero doit etre composé au moins de 7 chiffres',
      }),
    CNI: z.string().min(2, "c'est trop court").max(16, "c'est trop long"),
    role: z.enum(['ROLE_PROPRIETAIRE', 'ROLE_LOCATAIRE']),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: 'les mots de passe sont differents',
    path: ['confirmPassword'],
  })

export type RegisterFormType = z.infer<typeof RegisterSchema>

export function RegisterForm({
  nomUtilisateur = '',
  email = '',
  telephone = '',
  CNI = '',
  role = 'ROLE_PROPRIETAIRE',
  onSoumis,
}: {
  nomUtilisateur?: string
  email?: string
  telephone?: string
  CNI?: string
  role?: 'ROLE_PROPRIETAIRE' | 'ROLE_LOCATAIRE'
  onSoumis: ({
    nomUtilisateur,
    email,
    password,
    confirmPassword,
    telephone,
    CNI,
    role,
  }: RegisterFormType) => void
}) {
  // 1. Define your form.
  const form = useForm<z.infer<typeof RegisterSchema>>({
    resolver: zodResolver(RegisterSchema),
    defaultValues: {
      nomUtilisateur: nomUtilisateur,
      email: email,
      telephone: telephone,
      CNI: CNI,
      role: role,
    },
  })

  function onSubmit(values: z.infer<typeof RegisterSchema>) {
    onSoumis(values)
  }

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="space-y-4 flex flex-col"
      >
        <div className="grid grid-cols-2 gap-2 ">
          <FormField
            control={form.control}
            name="nomUtilisateur"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-center text-Nexa text-noir font-[800] ">
                  nom complet
                </FormLabel>
                <FormControl>
                  <Input placeholder="ex:mame aboulaye wade" {...field} />
                </FormControl>
                <FormDescription>
                  veuillez renseigner votre nom complet
                </FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-center text-Nexa text-noir font-[800] ">
                  email
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
        </div>
        <div className="grid grid-cols-2 gap-2 ">
          <FormField
            control={form.control}
            name="telephone"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-center text-Nexa text-noir font-[800] ">
                  numero de telephone
                </FormLabel>
                <FormControl>
                  <PhoneInput
                    placeholder="ex: 77 777 77 77"
                    {...field}
                    defaultCountry="SN"
                  />
                </FormControl>
                <FormDescription>
                  veuillez renseigner votre numero de telephone
                </FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="CNI"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-center text-Nexa text-noir font-[800] ">
                  numero CNI
                </FormLabel>
                <FormControl>
                  <Input {...field} type="number" />
                </FormControl>
                <FormDescription>veuillez renseigner votre CNI</FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        <div className="grid grid-cols-2 gap-2 ">
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
          <FormField
            control={form.control}
            name="confirmPassword"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-center text-Nexa text-noir font-[800] ">
                  confirmer
                </FormLabel>
                <FormControl>
                  <Input type="password" {...field} />
                </FormControl>
                <FormDescription>
                  veuillez confirmer le mot de passe
                </FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        <div>
          <FormField
            control={form.control}
            name="role"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Role</FormLabel>
                <Select
                  onValueChange={field.onChange}
                  defaultValue={field.value}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="choisissez le role" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectItem value="ROLE_PROPRIETAIRE">proprietaire</SelectItem>
                    <SelectItem value="ROLE_LOCATAIRE">locataire</SelectItem>
                  </SelectContent>
                </Select>
                <FormDescription>
                  le role du compte qui sera créé
                </FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>

        <Button type="submit">S'inscrire</Button>
      </form>
    </Form>
  )
}
