import React, { useState } from 'react'
import { toast } from 'sonner'
import type {InfoContratFormType} from '@/blocs/proprietaire/forms/contrat/info-contrat-form';
import { api } from '@/api/api'
import { InfoContratForm  } from '@/blocs/proprietaire/forms/contrat/info-contrat-form'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import { publish } from '@/lib/events'

type Props = {
    locataireId: number
    chambreId: number
}

const CreateContratButton = (props: Props) => {
  const [open, setOpen] = useState(false)

  const createContrat = api.contrat.create()

  function handleAddContrat(values: InfoContratFormType) {   
    console.log(values) 
    createContrat.mutate(
      {
        ...values,
        ...props
      },
      {
        onSettled(data, error, variables, context) {},
        async onSuccess(data, variables, context) {
          toast('Creation effectuée', {
            description: "La creation de l'ingredient est un succés",
          })
          publish("refresh_maison_chambre_table",{})
          publish("refresh_chambre_table",{})
          setOpen(false)
        },
        onError(error, variables, context) {
          toast.error('Erreur !!!', {
            description: " Une erreur s'est produite lors de la creation",
          })
        },
      },
    )
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Actions rapides</CardTitle>
      </CardHeader>
      <CardContent className="space-y-2">
        <Dialog open={open} onOpenChange={setOpen}>
          <DialogTrigger asChild>
            <Button
              variant="default"
              onClick={() => setOpen(true)}
              className="w-full justify-start"
            >
              Créer un contrat
            </Button>
          </DialogTrigger>
          <DialogContent className="min-w-screen min-h-screen">
            <DialogHeader>
              <DialogTitle>Créer un contrat</DialogTitle>
              <DialogDescription>
                Remplissez les informations pour créer un nouveau contrat.
              </DialogDescription>
            </DialogHeader>
            <InfoContratForm onSoumis={handleAddContrat} />
          </DialogContent>
        </Dialog>
      </CardContent>
    </Card>
  )
}

export default CreateContratButton
