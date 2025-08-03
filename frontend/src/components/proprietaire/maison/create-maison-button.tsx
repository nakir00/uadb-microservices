import React, { useState } from 'react'
import { toast } from 'sonner'
import type { InfoMaisonFormType } from '@/blocs/proprietaire/forms/maison/info-maison-form'
import { api } from '@/api/api'
import { InfoMaisonForm } from '@/blocs/proprietaire/forms/maison/info-maison-form'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'

type Props = {
  onCreateMaison?: () => void
}

const CreateMaisonButton = ({ onCreateMaison }: Props) => {
  const [isOpen, setIsOpen] = useState<boolean>(false)
  const maisonCreate = api.maison.create()

  function handleAddMaisonType(values: InfoMaisonFormType) {
    maisonCreate.mutate(
      {
        adresse: values.adresse,
        description: values.description,
        longitude: Number(values.lon),
        latitude: Number(values.lat),
      },
      {
        onSettled(data, error, variables, context) {},
        async onSuccess(data, variables, context) {
          toast('Creation effectuée', {
            description: "La creation de l'ingredient est un succés",
          })
          if (onCreateMaison) {
            onCreateMaison()
          }
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
    <>
      <Dialog open={isOpen} onOpenChange={setIsOpen}>
        <DialogTrigger asChild>
          <Button className="px-3">Nouvelle Maison</Button>
        </DialogTrigger>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Nouvelle Maison</DialogTitle>
            <DialogDescription>
              remplissez le formulaire pour creer une maison
            </DialogDescription>
            <div>
              <InfoMaisonForm
                onSoumis={handleAddMaisonType}
                description={undefined}
                adresse={undefined}
                lat={undefined}
                lon={undefined}
              />
            </div>
          </DialogHeader>
        </DialogContent>
      </Dialog>
    </>
  )
}

export default CreateMaisonButton
