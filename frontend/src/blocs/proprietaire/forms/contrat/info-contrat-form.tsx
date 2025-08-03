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
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'

const InfoContratFormSchema = z.object({
  dateDebut: z.string(),
  dateFin: z.string(),
  montantCaution: z.number(),
  moisCaution: z.number(),
  description: z.string(),
  modePaiement: z.enum(['VIREMENT', 'CASH', 'MOBILEMONEY']),
  periodicite: z.enum(['JOURNALIER', 'HEBDOMADAIRE', 'MENSUEL']),
  statut: z.enum(['ACTIF', 'RESILIE'])
})

export type InfoContratFormType = z.infer<typeof InfoContratFormSchema>

export function InfoContratForm({
  dateDebut = undefined,
  dateFin = undefined,
  montantCaution = undefined,
  moisCaution = undefined,
  description = undefined,
  modePaiement = undefined,
  periodicite = undefined,
  statut = undefined,
  onSoumis,
}: {
  dateDebut?: string
  dateFin?: string
  montantCaution?: number
  moisCaution?: number
  description?: string
  modePaiement?: InfoContratFormType['modePaiement']
  periodicite?: InfoContratFormType['periodicite']
  statut?: InfoContratFormType['statut']
  onSoumis: ({
    dateDebut,
    dateFin,
    montantCaution,
    moisCaution,
    description,
    modePaiement,
    periodicite,
    statut,
  }: InfoContratFormType) => void
}) {
  // 1. Define your form.
  const form = useForm<z.infer<typeof InfoContratFormSchema>>({
    resolver: zodResolver(InfoContratFormSchema),
    defaultValues: {
      dateDebut: dateDebut,
      dateFin: dateFin,
      montantCaution: montantCaution,
      moisCaution: moisCaution,
      description: description,
      modePaiement: modePaiement,
      periodicite: periodicite,
      statut: statut,
    },
  })

  function onSubmit(values: z.infer<typeof InfoContratFormSchema>) {
    onSoumis(values)
  }

  return (
    <div>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <FormField
                      control={form.control}
                      name="dateDebut"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Date de début</FormLabel>
                          <FormControl>
                            <Input
                              type="datetime-local"
                              step="1"
                              {...field}
                              value={
                                field.value
                                  ? new Date(field.value).toISOString().slice(0, 19)
                                  : ''
                              }
                              onChange={(e) =>
                                field.onChange(
                                  e.target.value
                                    ? new Date(e.target.value).toISOString()
                                    : undefined
                                )
                              }
                            />
                          </FormControl>
                          <FormDescription>Date de début du contrat</FormDescription>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                    <FormField
                      control={form.control}
                      name="dateFin"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Date de fin</FormLabel>
                          <FormControl>
                            <Input
                              type="datetime-local"
                              step="1"
                              {...field}
                              value={
                                field.value
                                  ? new Date(field.value).toISOString().slice(0, 19)
                                  : ''
                              }
                              onChange={(e) =>
                                field.onChange(
                                  e.target.value
                                    ? new Date(e.target.value).toISOString()
                                    : undefined
                                )
                              }
                            />
                          </FormControl>
                          <FormDescription>Date de fin du contrat</FormDescription>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <FormField
                      control={form.control}
                      name="montantCaution"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Montant Caution</FormLabel>
                          <FormControl>
                            <Input
                              type="number"
                              placeholder="ex: 50000"
                              {...field}
                              value={field.value ?? ''}
                              onChange={(e) =>
                                field.onChange(
                                  e.target.value ? Number(e.target.value) : undefined
                                )
                              }
                            />
                          </FormControl>
                          <FormDescription>Montant de la caution (FCFA)</FormDescription>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                    <FormField
                      control={form.control}
                      name="moisCaution"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Mois de caution</FormLabel>
                          <FormControl>
                            <Input
                              type="number"
                              placeholder="ex: 2"
                              {...field}
                              value={field.value ?? ''}
                              onChange={(e) =>
                                field.onChange(
                                  e.target.value ? Number(e.target.value) : undefined
                                )
                              }
                            />
                          </FormControl>
                          <FormDescription>Nombre de mois de caution</FormDescription>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>
                  <FormField
                    control={form.control}
                    name="description"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Description</FormLabel>
                        <FormControl>
                          <Textarea
                            placeholder="Détails sur le contrat"
                            className="resize-none"
                            {...field}
                            value={field.value ?? ''}
                          />
                        </FormControl>
                        <FormDescription>Informations sur le contrat</FormDescription>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <FormField
                      control={form.control}
                      name="modePaiement"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Mode de paiement</FormLabel>
                          <Select onValueChange={field.onChange} value={field.value}>
                            <FormControl>
                              <SelectTrigger>
                                <SelectValue placeholder="Sélectionner un mode" />
                              </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                              {['VIREMENT', 'CASH', 'MOBILEMONEY'].map((mode) => (
                                <SelectItem key={mode} value={mode}>
                                  {mode.toLowerCase()}
                                </SelectItem>
                              ))}
                            </SelectContent>
                          </Select>
                          <FormDescription>Mode de paiement du contrat</FormDescription>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                    <FormField
                      control={form.control}
                      name="periodicite"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Périodicité</FormLabel>
                          <Select onValueChange={field.onChange} value={field.value}>
                            <FormControl>
                              <SelectTrigger>
                                <SelectValue placeholder="Sélectionner une périodicité" />
                              </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                              {['JOURNALIER', 'HEBDOMADAIRE', 'MENSUEL'].map((periode) => (
                                <SelectItem key={periode} value={periode}>
                                  {periode.toLowerCase()}
                                </SelectItem>
                              ))}
                            </SelectContent>
                          </Select>
                          <FormDescription>Périodicité du contrat</FormDescription>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                    <FormField
                      control={form.control}
                      name="statut"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Statut</FormLabel>
                          <Select onValueChange={field.onChange} value={field.value}>
                            <FormControl>
                              <SelectTrigger>
                                <SelectValue placeholder="Sélectionner un statut" />
                              </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                              {['ACTIF', 'RESILIE'].map((statut) => (
                                <SelectItem key={statut} value={statut}>
                                  {statut.toLowerCase()}
                                </SelectItem>
                              ))}
                            </SelectContent>
                          </Select>
                          <FormDescription>Statut du contrat</FormDescription>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>
                  <Button type="submit">Créer Contrat</Button>
          <Button type="submit">Creer Contrat</Button>
        </form>
      </Form>
    </div>
  )
}
