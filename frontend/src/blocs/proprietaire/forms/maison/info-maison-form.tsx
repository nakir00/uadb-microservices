import { number, z } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'

const InfoMaisonFormSchema = z.object({
  description: z.string().min(2, "c'est trop court").max(25, "c'est trop long"),
  adresse: z.string().min(2, "c'est trop court").max(25, "c'est trop long"),
  lat: z.string().max(100),
  lon: z.string().max(100)
})

export type InfoMaisonFormType = z.infer<typeof InfoMaisonFormSchema>

export function InfoMaisonForm({
  description = undefined,
  adresse = undefined,
  lat = undefined,
  lon = undefined,
  onSoumis,
}: {
  description: string | undefined
  adresse: string | undefined
  lat: number | undefined
  lon: number | undefined
  onSoumis: ({ description, adresse, lat, lon }: InfoMaisonFormType) => void
}) {
  // 1. Define your form.
  const form = useForm<z.infer<typeof InfoMaisonFormSchema>>({
    resolver: zodResolver(InfoMaisonFormSchema),
    defaultValues: {
      description: description??"",
      adresse: adresse??"",
      lat: lat?.toString(),
      lon: lon?.toString(),
    },
  })

  function onSubmit(values: z.infer<typeof InfoMaisonFormSchema>) {
    onSoumis(values);
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
                <FormLabel>description</FormLabel>
                <FormControl>
                   <Input placeholder="ex: maison almadies" {...field} />
                </FormControl>
                <FormDescription>
                une description ou nom de la maison
              </FormDescription>
              <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="adresse"
            render={({ field }) => (
              <FormItem>
                <FormLabel>adresse</FormLabel>
                <FormControl>
                   <Input placeholder="391 almadies dakar" {...field} />
                </FormControl>
                <FormDescription>
                l'adresse de la maison
              </FormDescription>
              <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="lat"
            render={({ field }) => (
              <FormItem>
                <FormLabel>latitude</FormLabel>
                <FormControl>
                   <Input placeholder="39.3848940"  {...field} type="number" />
                </FormControl>
                <FormDescription>
                latitude en geolocalisation
              </FormDescription>
              <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="lon"
            render={({ field }) => (
              <FormItem>
                <FormLabel>longitude</FormLabel>
                <FormControl>
                   <Input placeholder="-23.49490494" {...field} type='number' />
                </FormControl>
                <FormDescription>
                longitude en geolocalisation
              </FormDescription>
              <FormMessage />
              </FormItem>
            )}
          />
          <Button type="submit">S'inscrire</Button>
        </form>
      </Form>
    </div>
  )
}
