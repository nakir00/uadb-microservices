import { Ban, CircleOff, Eye, MoreHorizontal, SquarePen, Trash } from 'lucide-react'
import { useState } from 'react'
import { toast } from 'sonner'
import { Link, useNavigate } from '@tanstack/react-router'
import { RiCheckLine, RiVerifiedBadgeFill } from '@remixicon/react'
import { Description } from '@radix-ui/react-dialog'
import { InfoChambreForm } from '../../forms/maison/info-chambre-form'
import type { ColumnDef } from '@tanstack/react-table'
import type {InfoMaisonFormType} from '../../forms/maison/info-maison-form';
import type { ChambreModel } from '@/api/queries/chambre'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { api } from '@/api/api'
import { publish } from '@/lib/events'
import { Badge } from '@/components/ui/badge'
import { cn } from '@/lib/utils'

export const ChambreColumns: Array<ColumnDef<ChambreModel>> = [
  {
    accessorKey: 'id',
    header: 'id',
    size: 10,
  },
  {
    accessorKey: 'titre',
    header: 'Titre',
  },
  {
    accessorKey: 'taille',
    header: 'Taille',
    size: 70
  },
  {
    header: "type",
    accessorKey: "Type",
    cell: ({ row }) => {

      const chambre = row.original

      return (<div className="flex items-center h-full">
        <Badge
          variant="outline"
          className={cn(
            "gap-1 py-0.5 px-2 text-sm text-primary",
          )}
        >
          {chambre.type.toLowerCase()}
        </Badge>
      </div>)
    },
    size: 110,
  },
  {
    accessorKey: 'prix',
    header: 'Prix',
    cell:({ row }) => {

      const chambre = row.original

      return (<div className="flex items-center h-full justify-evenly">
        <span>{chambre.prix.toString()}</span>
        <Badge
          variant="outline"
          className={cn(
            "gap-1 py-0.5 px-2 text-sm text-primary",
          )}
        >
          F.CFA
        </Badge>
      </div>)
    },
  },
  {
    header: "meublé",
    accessorKey: "meublee",
    cell: ({ row }) => {

      const chambre = row.original

      return (
      <div>
        <span className="sr-only">
          {row.original.meublee ? "Meublee" : "Not meublee"}
        </span>
        {chambre.meublee ? <RiCheckLine className="text-emerald-600" /> : <Ban className="text-red-500/50" />}
      </div>)
      },
    size: 80,
  },
  {
    header: "salle de bain",
    accessorKey: "salleDeBain",
    cell: ({ row }) => {

      const chambre = row.original


      return (
      <div>
        <span className="sr-only">
          {row.original.meublee ? "Meublee" : "Not meublee"}
        </span>
        {chambre.salleDeBain ? <RiCheckLine className="text-emerald-600" /> : <Ban className="text-red-500/50" />}
      </div>)
      },
    size: 80,
  },
  {
    header: "disponible",
    accessorKey: "disponible",
    cell: ({ row }) => {

      const chambre = row.original

      return (
      <div>
        <span className="sr-only">
          {chambre.disponible ? "disponible" : "Not disponible"}
        </span>
        {chambre.disponible ? <RiCheckLine className="text-emerald-600" /> : <Ban className="text-red-500/50" />}
      </div>)
      },
    size: 80,
  },
  {
    accessorKey: 'creeLe',
    header: 'cree Le :',
  },
  {
    id: 'page',
    cell: ({ row }) => {
      const chambre = row.original
      if (!chambre.id) {
        return <Button disabled> <Eye /> </Button>
      }

      return <Link to={'/proprietaire/chambres/$chambreId'} params={{chambreId: chambre.id.toString()}}> <Button> <Eye /></Button></Link>
    },
    size: 70,
  },
  {
    id: 'actions',
    cell: ({ row }) => {
      const chambre = row.original
      const [open, setOpen] = useState(false)
      const maisonUpdate = api.chambre.update()
      const maisonDelete = api.chambre.delete() 
      const [triggered, setTriggered] = useState('')

      function closeDialog() {
        setOpen(false)
      }

      const deleteId = () => {
        if(!chambre.id){
          toast.error('Erreur !!!',{
                description:
                  "ligne non définie",
              })
          return
        }

        maisonDelete.mutate(chambre.id, {
          onSettled(data, error, variables, context) {
            closeDialog()
            publish('refresh_chambre_table',{})
          },
          onSuccess(data, variables, context) {
            toast.success('Suppression effectuée',{
              description: "La suppression de la maison est un succés",
            })
          },
          onError(error, variables, context) {
            toast.error('Erreur !!!',{
              description: " Une erreur s'est produite lors de la supression",
            })
          },
        })
      }

      const updateId = ({ titre, taille, type, description, meublee, salleDeBain, disponible, prix, }: { titre: string; taille: string; description: string; type: 'SIMPLE' | 'APPARTEMENT' | 'MAISON'; meublee: boolean; salleDeBain: boolean; disponible: boolean; prix: number }) => {
    
        if(!chambre.id){
          toast.error('Erreur !!!',{
                description:
                  "ligne non définie",
              })
          return
        }

        const formData = new FormData()
        formData.append('id', chambre.id.toString())
        formData.append('titre', titre)
        formData.append('taille', taille)
        formData.append('type', type)
        formData.append('description', description)
        formData.append('meublee', meublee.toString())
        formData.append('salleDeBain', salleDeBain.toString())
        formData.append('disponible', disponible.toString())
        formData.append('prix', prix.toString())
        
        maisonUpdate.mutate({data: formData, id: chambre.id} ,
          {
            onSettled(data, error, variables, context) { 
              closeDialog()
              publish('refresh_chambre_table',{})
            },
            onSuccess(data, variables, context) {
              toast.success('Modification effectuée',{
                description: "La modification de l'unité est un succés",
              })
            },
            onError(error, variables, context) {
              toast.error('Erreur !!!',{
                description:
                  " Une erreur s'est produite lors de la modification",
              })
            },
          },
        )
      }

      return (
        <Dialog open={open} onOpenChange={setOpen}>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" className="w-8 h-8 p-0">
                <span className="sr-only">Open menu</span>
                <MoreHorizontal className="w-4 h-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuLabel>Actions</DropdownMenuLabel>

              <DialogTrigger asChild>
                <DropdownMenuItem
                  className="flex flex-row px-2 justify-between"
                  onClick={() => {
                    setTriggered('modifier')
                  }}
                >
                  <span>modifier</span>
                  <SquarePen />
                </DropdownMenuItem>
              </DialogTrigger>

              <DialogTrigger asChild>
                <DropdownMenuItem
                  className="flex flex-row px-2 justify-between text-red-500"
                  onClick={() => {
                    setTriggered('delete')
                  }}
                >
                  Supprimer
                  <Trash />
                </DropdownMenuItem>
              </DialogTrigger>
            </DropdownMenuContent>
          </DropdownMenu>
          <DialogContent className='min-w-3/5'>
            {triggered === 'modifier' && (
              <>
                <DialogHeader>
                  <DialogTitle>Modification</DialogTitle>
                  <DialogDescription>
                    modification de {chambre.titre}
                  </DialogDescription>
                </DialogHeader>
                <div className="p-8 md:p-0">
                  <InfoChambreForm onSoumis={updateId}     
                  titre={chambre.titre}
                  taille={chambre.taille}
                  type={chambre.type}
                  description={chambre.description}
                  meublee={chambre.meublee}
                  salleDeBain={chambre.salleDeBain}
                  disponible={chambre.disponible}
                  prix={chambre.prix.toString()}             
                   
                   />
                 
                </div>
              </>
            )}

            {triggered === 'delete' && (
              <>
                <DialogHeader>
                  <DialogTitle>Supression Maison</DialogTitle>
                  <DialogDescription>
                    etes vous sur de vouloir suprimer cette maison ?
                  </DialogDescription>
                </DialogHeader>
                <DialogFooter>
                  <div className="flex flex-row w-full gap-4 mb-8">
                    <Button
                      className="w-1/3"
                      variant={'destructive'}
                      onClick={() => deleteId()}
                    >
                      OUI
                    </Button>
                    <Button
                      className="w-2/3"
                      onClick={() => {
                        closeDialog()
                      }}
                    >
                      NON
                    </Button>
                  </div>
                </DialogFooter>
              </>
            )}

          </DialogContent>
        </Dialog>
      )
    },
  },
]
