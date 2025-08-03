import React, { useState } from 'react'
import { toast } from 'sonner'
import type {ProblemeFormType} from '@/blocs/locataire/forms/probleme/info-probleme-form';
import { api } from '@/api/api'
import { ProblemeForm  } from '@/blocs/locataire/forms/probleme/info-probleme-form'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import { useUser } from '@/hooks/user';

type Props = {
  onCreate?: () => void
  contratId: number
}

const CreateProblemeButton = ({ onCreate,contratId }: Props) => {
  const { user } = useUser()
  const [isOpen, setIsOpen] = useState<boolean>(false)
  const createProbleme = api.probleme.create()

  function handleAddProbleme({
    description,
    type,
    responsable,
    resolu,
  }: ProblemeFormType) {
    createProbleme.mutate(
      {
        contratId,
        signalePar: user!.id!,
        description,
        type,
        responsable,
        resolu,
      },
      {
        onSettled(data, error, variables, context) {},
        async onSuccess(data, variables, context) {
          toast.success('Creation effectuée', {
            description: 'La creation du rendez-vous est un succés',
          })
          // publish('refresh_locataire_rendez_vous_table', {})
          //          publish('refresh_chambre_table', {})
          if(onCreate)onCreate()
          setIsOpen(false)
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
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        <Button onClick={() => setIsOpen(true)} className="px-3">
          signaler un probleme
        </Button>
      </DialogTrigger>
      <DialogContent className="min-w-3/5">
        <DialogHeader>
          <DialogTitle>Alerter Probleme</DialogTitle>
          <DialogDescription>
            alerter un probleme dans le bien immobilier
          </DialogDescription>
          <div>
            <ProblemeForm onSoumis={handleAddProbleme} />
          </div>
        </DialogHeader>
      </DialogContent>
    </Dialog>
  )
}

export default CreateProblemeButton
