import { number, z } from 'zod'
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
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group'
import { Switch } from '@/components/ui/switch'
import { Textarea } from '@/components/ui/textarea'

const InfoChambreFormSchema = z.object({
  titre: z.string().min(2, "c'est trop court").max(25, "c'est trop long"),
  taille: z.string().min(2, "c'est trop court"),
  description: z.string().min(2, "c'est trop court"),
  type: z.enum(['SIMPLE', 'APPARTEMENT', 'MAISON'], {
    required_error: 'vous devez selectionner au moins un',
  }),
  meublee: z.boolean(),
  salleDeBain: z.boolean(),
  disponible: z.boolean(),
  prix: z.string(),
})

export type InfoChambreFormType = z.infer<typeof InfoChambreFormSchema>

export function InfoChambreForm({
  titre = undefined,
  taille = undefined,
  description = undefined,
  type = undefined,
  meublee = false,
  salleDeBain = false,
  disponible = false,
  prix = undefined,
  onSoumis,
}: {
  titre?: string
  taille?: string
  description?: string
  type?: 'SIMPLE' | 'APPARTEMENT' | 'MAISON'
  meublee?: boolean
  salleDeBain?: boolean
  disponible?: boolean
  prix?: string
  onSoumis: ({
    titre,
    taille,
    type,
    description,
    meublee,
    salleDeBain,
    disponible,
    prix,
  }: {
    titre: string
    taille: string
    description: string
    type: 'SIMPLE' | 'APPARTEMENT' | 'MAISON'
    meublee: boolean
    salleDeBain: boolean
    disponible: boolean
    prix: number
  }) => void
}) {
  // 1. Define your form.
  const form = useForm<z.infer<typeof InfoChambreFormSchema>>({
    resolver: zodResolver(InfoChambreFormSchema),
    defaultValues: {
      titre: titre,
      taille: taille,
      description: description,
      type: type,
      meublee: meublee,
      salleDeBain: salleDeBain,
      disponible: disponible,
      prix: prix,
    },
  })

  function onSubmit(values: InfoChambreFormType) {    
    onSoumis({ ...values, prix: Number(values.prix) })
  }

  return (
    <div>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="space-y-8 flex flex-col w-full"
        >
          <div className=" flex flex-row w-full justify-between gap-2">
            <FormField
            
              control={form.control}
              name="titre"
              render={({ field }) => (
                <FormItem className='w-full'>
                  <FormLabel>titre</FormLabel>
                  <FormControl>
                    <Input className='w-full' placeholder="ex: Chambre abcd" {...field} />
                  </FormControl>
                  <FormDescription>la titre de la Chambre</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="taille"
              render={({ field }) => (
                <FormItem className='w-full'>
                  <FormLabel>taille</FormLabel>
                  <FormControl>
                    <Input className='w-full' placeholder="50 m2" {...field}  endAdornment={<>m²</>}/>
                  </FormControl>
                  <FormDescription> la taille de la Chambre</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="prix"
              render={({ field }) => (
                <FormItem className='w-full'>
                  <FormLabel>prix</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="50.000"
                      {...field}
                      className='w-full'
                      type="number"
                      endAdornment={<>FCFA</>}
                    />
                  </FormControl>
                  <FormDescription>le prix donnée</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          <div className=" flex flex-row w-full justify-between gap-2">
            <FormField
              control={form.control}
              name="meublee"
              render={({ field }) => (
                <FormItem className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-sm w-full">
                  <div className="space-y-0.5">
                    <FormLabel>Meublée</FormLabel>
                    <FormDescription>
                      l'appartement est -il meublée
                    </FormDescription>
                  </div>
                  <FormControl>
                    <Switch
                      checked={field.value}
                      onCheckedChange={field.onChange}
                    />
                  </FormControl>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="salleDeBain"
              render={({ field }) => (
                <FormItem className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-sm w-full">
                  <div className="space-y-0.5">
                    <FormLabel>salle de bain</FormLabel>
                    <FormDescription>
                      l'appartement possede t-il une salle de bain
                    </FormDescription>
                  </div>
                  <FormControl>
                    <Switch
                      checked={field.value}
                      onCheckedChange={field.onChange}
                    />
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="disponible"
              render={({ field }) => (
                <FormItem className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-sm w-full">
                  <div className="space-y-0.5">
                    <FormLabel>Disponible</FormLabel>
                    <FormDescription>
                      l'appartement est -il disponible
                    </FormDescription>
                  </div>
                  <FormControl>
                    <Switch
                      checked={field.value}
                      onCheckedChange={field.onChange}
                    />
                  </FormControl>
                </FormItem>
              )}
            />
          </div>
          <div className=" flex flex-row w-full">
            <FormField
              control={form.control}
              name="type"
              render={({ field }) => (
                <FormItem className="space-y-3 w-1/3">
                  <FormLabel>Type</FormLabel>
                  <FormControl>
                    <RadioGroup
                      onValueChange={field.onChange}
                      defaultValue={field.value}
                      className="flex flex-col"
                    >
                      {['SIMPLE', 'APPARTEMENT', 'MAISON'].map((types,index) => (
                          <FormItem key={index} className="flex items-center gap-3">
                            <FormControl>
                              <RadioGroupItem value={types} />
                            </FormControl>
                            <FormLabel className="font-normal">
                              {types.toLowerCase()}
                            </FormLabel>
                          </FormItem>
                      ))}
                    </RadioGroup>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="description"
              render={({ field }) => (
                <FormItem className='w-2/3'>
                  <FormLabel>Description</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="dites en plus sur la chambre"
                      className="resize-none"
                      {...field}
                    />
                  </FormControl>
                  <FormDescription>
                    plus d'informations sur la chambre
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <Button type="submit">valider</Button>
        </form>
      </Form>
    </div>
  )
}
