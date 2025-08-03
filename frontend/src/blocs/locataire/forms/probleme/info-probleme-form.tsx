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
import { Button } from '@/components/ui/button'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'
import { Checkbox } from '@/components/ui/checkbox'

const ProblemeFormSchema = z.object({
  description: z.string().min(1, "La description est requise"),
  type: z.enum(['PLOMBERIE', 'ELECTRICITE', 'AUTRE']),
  responsable: z.enum(['LOCATAIRE', 'PROPRIETAIRE']),
  resolu: z.boolean(),
})

export type ProblemeFormType = z.infer<typeof ProblemeFormSchema>

export function ProblemeForm({
  description = undefined,
  type = undefined,
  responsable = undefined,
  resolu = false,
  onSoumis,
}: {
  description?: string
  type?: ProblemeFormType['type']
  responsable?: ProblemeFormType['responsable']
  resolu?: boolean
  onSoumis: ({
    description,
    type,
    responsable,
    resolu,
  }: ProblemeFormType) => void
}) {
  // 1. Define your form.
  const form = useForm<z.infer<typeof ProblemeFormSchema>>({
    resolver: zodResolver(ProblemeFormSchema),
    defaultValues: {
      description: description,
      type: type,
      responsable: responsable,
      resolu: resolu,
    },
  })

  function onSubmit(values: z.infer<typeof ProblemeFormSchema>) {
    onSoumis(values)
  }

  return (
    <div>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
          <FormField
            control={form.control}
            name="description"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Description du problème</FormLabel>
                <FormControl>
                  <Textarea
                    placeholder="Décrivez le problème rencontré"
                    className="resize-none"
                    {...field}
                    value={field.value}
                  />
                </FormControl>
                <FormDescription>Détails sur le problème signalé</FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="type"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Type de problème</FormLabel>
                  <Select onValueChange={field.onChange} value={field.value}>
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Sélectionner un type" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {['PLOMBERIE', 'ELECTRICITE', 'AUTRE'].map((typeProbleme) => (
                        <SelectItem key={typeProbleme} value={typeProbleme}>
                          {typeProbleme.toLowerCase()}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormDescription>Catégorie du problème</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="responsable"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Responsable</FormLabel>
                  <Select onValueChange={field.onChange} value={field.value}>
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Sélectionner le responsable" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {['LOCATAIRE', 'PROPRIETAIRE'].map((resp) => (
                        <SelectItem key={resp} value={resp}>
                          {resp.toLowerCase()}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormDescription>Qui est responsable de la résolution</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <FormField
            control={form.control}
            name="resolu"
            render={({ field }) => (
              <FormItem className="flex flex-row items-start space-x-3 space-y-0">
                <FormControl>
                  <Checkbox
                    checked={field.value}
                    onCheckedChange={field.onChange}
                  />
                </FormControl>
                <div className="space-y-1 leading-none">
                  <FormLabel>
                    Problème résolu
                  </FormLabel>
                  <FormDescription>
                    Cochez si le problème a été résolu
                  </FormDescription>
                </div>
              </FormItem>
            )}
          />

          <Button type="submit">Signaler le problème</Button>
        </form>
      </Form>
    </div>
  )
}