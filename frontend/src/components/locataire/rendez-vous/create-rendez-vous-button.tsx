import React, { useState } from 'react'
import { toast } from 'sonner'
import type { DefinedUseQueryResult } from '@tanstack/react-query'
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
import { Badge } from '@/components/ui/badge'
import { InfoRendezVousForm } from '@/blocs/locataire/forms/rendez-vous/info-rendez-vous-form'
import { publish } from '@/lib/events'

type Props = {
  chambreId?: number
  locataireId: number
}

const CreateRendezVousButton = ({ chambreId, locataireId }: Props) => {
  const [isOpen, setIsOpen] = useState<boolean>(chambreId !== undefined)
  const createRendezVous = api.rendezVous.create()

  let chambreData: DefinedUseQueryResult<unknown, Error>

  if (chambreId) {
    chambreData = api.chambre.AsGuestgetOne(chambreId)
  }

  function handleAddRendezVous({ time }: { time: Date }) {
    createRendezVous.mutate(
      {
        chambreId: chambreId!,
        dateHeure: time,
        locataireId: locataireId,
      },
      {
        onSettled(data, error, variables, context) {},
        async onSuccess(data, variables, context) {
          toast('Creation effectuée', {
            description: "La creation du rendez-vous est un succés",
          })
           publish('refresh_locataire_rendez_vous_table', {})
//          publish('refresh_chambre_table', {})
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
      <Dialog open={isOpen} onOpenChange={setIsOpen}>
        {/* <DialogTrigger asChild>
          <Button onClick={()=>setIsOpen(true)} className="px-3">Nouvelle Chambre</Button>
        </DialogTrigger> */}
        <DialogContent className="min-w-3/5">
          <DialogHeader>
            <DialogTitle>Nouveau Rendez-vous</DialogTitle>
            <DialogDescription>
              {(chambreData && chambreData.isLoading) ? (
                <span> ca charge ...</span>
              ) : (
                <span>
                  remplissez le formulaire pour creer le rendez-vous avec <br />
                  <span>
                    {
                      chambreData.data!.data.maison.utilisateurDTO
                        .nomUtilisateur
                    }
                  </span>
                  <br />
                  <span>
                    chambre : <Badge>{chambreData.data!.data.titre}</Badge>
                  </span>
                  <br />
                  <span>
                    maison :{' '}
                    <Badge>{chambreData.data!.data.maison.description}</Badge> à{' '}
                    <Badge>{chambreData.data!.data.maison.adresse}</Badge>
                  </span>
                  <br />
                </span>
              )}
            </DialogDescription>
            <div>
              <InfoRendezVousForm onSoumis={handleAddRendezVous} />
            </div>
          </DialogHeader>
        </DialogContent>
      </Dialog>
    </>
  )
}

export default CreateRendezVousButton
