import React, { useState } from 'react'
import { toast } from 'sonner'
import { api } from '@/api/api'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import {
  InfoChambreForm
  
} from '@/blocs/proprietaire/forms/maison/info-chambre-form'
import { publish } from '@/lib/events';

type Props = {
  maisonId: number
}

const CreateChambreButton = ({ maisonId }: Props) => {
  const chambreCreate = api.chambre.create()
  const [isOpen, setIsOpen] = useState(false)

  function handleAddChambre(values: {
    titre: string;
    taille: string;
    description: string;
    type: "SIMPLE" | "APPARTEMENT" | "MAISON";
    meublee: boolean;
    salleDeBain: boolean;
    disponible: boolean;
    prix: number;
}) {    
    chambreCreate.mutate(
      {
        ...values, maisonId: maisonId
      },
      {
        onSettled(data, error, variables, context) {},
        async onSuccess(data, variables, context) {
          toast('Creation effectuée', {
            description: "La creation de l'ingredient est un succés",
          })
          publish("refresh_maison_chambre_table",{})
          publish("refresh_chambre_table",{})
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
    <>
      <Dialog open={isOpen} onOpenChange={setIsOpen} >
        <DialogTrigger asChild>
          <Button onClick={()=>setIsOpen(true)} className="px-3">Nouvelle Chambre</Button>
        </DialogTrigger>
        <DialogContent className='min-w-3/5'>
          <DialogHeader>
            <DialogTitle>Nouvelle Chambre</DialogTitle>
            <DialogDescription>
              remplissez le formulaire pour creer une chambre
            </DialogDescription>
            <div>
              <InfoChambreForm
                onSoumis={handleAddChambre}
              />
            </div>
          </DialogHeader>
        </DialogContent>
      </Dialog>
    </>
  )
}

export default CreateChambreButton
