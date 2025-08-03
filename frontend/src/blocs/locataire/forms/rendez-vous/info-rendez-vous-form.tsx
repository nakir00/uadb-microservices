import { number, z } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import { toast } from 'sonner'
import { CalendarIcon } from 'lucide-react'
import { format } from 'date-fns'
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

import { cn } from '@/lib/utils'
import { Calendar } from '@/components/ui/calendar'
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover'
import { ScrollArea, ScrollBar } from '@/components/ui/scroll-area'
import { DateTimePicker } from '@/components/ui/shadc-ui-expansion/datetime-picker'

const InfoRendezVousFormSchema = z.object({
  time: z.date({
    required_error: 'A date and time is required.',
  }),
})

export type InfoRendezVousFormType = z.infer<typeof InfoRendezVousFormSchema>

export function InfoRendezVousForm({
  time,
  onSoumis,
}: {
  time?: Date,
  onSoumis: ({time}: {time:Date}) => void
}) {
  // 1. Define your form.
  const form = useForm<z.infer<typeof InfoRendezVousFormSchema>>({
    resolver: zodResolver(InfoRendezVousFormSchema),
    defaultValues: {
      time: time
    },
  })

  function onSubmit(values: InfoRendezVousFormType) {
    onSoumis({ ...values })
  }

  return (
    <div>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-5">
          <FormField
            control={form.control}
            name="time"
            render={({ field }) => (
              <FormItem className="flex w-72 flex-col gap-2">
                <FormLabel htmlFor="datetime">Date time</FormLabel>
                <FormControl>
                  <DateTimePicker value={field.value} onChange={field.onChange} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <Button type="submit">Submit</Button>
        </form>
      </Form>
    </div>
  )
}
