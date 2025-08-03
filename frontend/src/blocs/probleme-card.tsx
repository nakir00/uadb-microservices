import { useState } from "react"
import { toast } from "sonner"
import { AlertCircle, Calendar, CheckCircle, HelpCircle, User, Wrench, Zap } from "lucide-react"
import type { ProblemeModel } from "@/api/queries/probleme"
import { api } from "@/api/api"
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
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"

interface ProblemeCardProps {
  probleme: ProblemeModel
  onMarquerResolu?: (problemeId: number) => void
}

export function ProblemeCard({ probleme, onMarquerResolu }: ProblemeCardProps) {
  const [isDialogOpen, setIsDialogOpen] = useState(false)

  const resoudreProbleme = api.probleme.resoudre()

  const handleResoudre = () => {

    resoudreProbleme.mutate(
      {
        id: probleme.id!,
      },
      {
        onSettled(data, error, variables, context) {},
        async onSuccess(data, variables, context) {
          toast.success('resolution effectuée', {
            description: 'La resolution est un succès',
          })
          handleMarquerResolu()
        },
        onError(error, variables, context) {
          toast.error('Erreur !!!', {
            description: " Une erreur s'est produite lors de la resolution",
          })
        },
      },
    )
  }

  const getTypeIcon = (type: string) => {
    switch (type) {
      case 'PLOMBERIE':
        return <Wrench className="w-4 h-4" />
      case 'ELECTRICITE':
        return <Zap className="w-4 h-4" />
      default:
        return <HelpCircle className="w-4 h-4" />
    }
  }

  const getTypeColor = (type: string) => {
    switch (type) {
      case 'PLOMBERIE':
        return 'bg-blue-100 text-blue-800'
      case 'ELECTRICITE':
        return 'bg-yellow-100 text-yellow-800'
      default:
        return 'bg-gray-100 text-gray-800'
    }
  }

  const getResponsableColor = (responsable: string) => {
    return responsable === 'PROPRIETAIRE'
      ? 'bg-purple-100 text-purple-800'
      : 'bg-green-100 text-green-800'
  }

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  }

  const handleMarquerResolu = () => {
    if (probleme.id && onMarquerResolu) {
      onMarquerResolu(probleme.id)
      setIsDialogOpen(false)
    }
  }

  return (
    <Card
      className={`w-full ${probleme.resolu ? 'bg-green-50 border-green-200' : 'bg-white'}`}
    >
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between">
          <div className="flex items-center gap-2">
            {getTypeIcon(probleme.type)}
            <CardTitle className="text-lg">
              {probleme.type.toLowerCase()}
            </CardTitle>
            {probleme.resolu && (
              <CheckCircle className="w-5 h-5 text-green-600" />
            )}
          </div>
          <div className="flex gap-2">
            <Badge className={getTypeColor(probleme.type)}>
              {probleme.type}
            </Badge>
            <Badge className={getResponsableColor(probleme.responsable)}>
              {probleme.responsable}
            </Badge>
          </div>
        </div>
      </CardHeader>

      <CardContent>
        <CardDescription className="text-sm text-gray-600 line-clamp-2">
          {probleme.description}
        </CardDescription>
        <div className="flex items-center gap-2 mt-3 text-xs text-gray-500">
          <Calendar className="w-3 h-3" />
          <span>Signalé le {formatDate(probleme.creeLe)}</span>
        </div>
      </CardContent>

      <CardFooter className="pt-2">
        <div className="flex items-center justify-between w-full">
          <div className="flex items-center gap-2">
            {probleme.resolu ? (
              <Badge className="bg-green-100 text-green-800">
                <CheckCircle className="w-3 h-3 mr-1" />
                Résolu
              </Badge>
            ) : (
              <Badge className="bg-red-100 text-red-800">
                <AlertCircle className="w-3 h-3 mr-1" />
                En attente
              </Badge>
            )}
          </div>

          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <DialogTrigger asChild>
              <Button variant="outline" size="sm">
                Détails
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[500px]">
              <DialogHeader>
                <DialogTitle className="flex items-center gap-2">
                  {getTypeIcon(probleme.type)}
                  Problème #{probleme.id} - {probleme.type.toLowerCase()}
                </DialogTitle>
                <DialogDescription>
                  Détails complets du problème signalé
                </DialogDescription>
              </DialogHeader>

              <div className="space-y-4">
                <div>
                  <h4 className="font-medium text-sm text-gray-700 mb-1">
                    Description
                  </h4>
                  <p className="text-sm bg-gray-50 p-3 rounded-md">
                    {probleme.description}
                  </p>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <h4 className="font-medium text-sm text-gray-700 mb-1">
                      Type
                    </h4>
                    <Badge className={getTypeColor(probleme.type)}>
                      {getTypeIcon(probleme.type)}
                      <span className="ml-1">{probleme.type}</span>
                    </Badge>
                  </div>

                  <div>
                    <h4 className="font-medium text-sm text-gray-700 mb-1">
                      Responsable
                    </h4>
                    <Badge
                      className={getResponsableColor(probleme.responsable)}
                    >
                      <User className="w-3 h-3 mr-1" />
                      {probleme.responsable}
                    </Badge>
                  </div>
                </div>

                <div>
                  <h4 className="font-medium text-sm text-gray-700 mb-1">
                    Date de signalement
                  </h4>
                  <div className="flex items-center gap-2 text-sm text-gray-600">
                    <Calendar className="w-4 h-4" />
                    {formatDate(probleme.creeLe)}
                  </div>
                </div>

                <div>
                  <h4 className="font-medium text-sm text-gray-700 mb-1">
                    Statut
                  </h4>
                  {probleme.resolu ? (
                    <Badge className="bg-green-100 text-green-800">
                      <CheckCircle className="w-3 h-3 mr-1" />
                      Résolu
                    </Badge>
                  ) : (
                    <Badge className="bg-red-100 text-red-800">
                      <AlertCircle className="w-3 h-3 mr-1" />
                      En attente de résolution
                    </Badge>
                  )}
                </div>
              </div>

              <DialogFooter className="gap-2">
                <Button
                  variant="outline"
                  onClick={() => setIsDialogOpen(false)}
                >
                  Fermer
                </Button>
                {!probleme.resolu && onMarquerResolu && (
                  <Button
                    onClick={()=>handleResoudre()}
                    className="bg-green-600 hover:bg-green-700"
                  >
                    <CheckCircle className="w-4 h-4 mr-2" />
                    Marquer comme résolu
                  </Button>
                )}
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </div>
      </CardFooter>
    </Card>
  )
}
