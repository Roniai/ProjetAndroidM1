<?php
/* Une collection est un ensemble de ressources.
Un lecteur est une ressource.
La liste des lecteurs est une collection. */

namespace App\Entity;

use ApiPlatform\Core\Annotation\ApiResource;
use App\Repository\LecteurRepository;
use Doctrine\ORM\Mapping as ORM;

/**
 * @ApiResource()
 * @ORM\Entity(repositoryClass=LecteurRepository::class)
 */

class Lecteur
{
    /**
     * @ORM\Id
     * @ORM\Column(type="string", length=25)
     */
    private $numlecteur;
    /**
     * @ORM\Column(type="string", length=100)
     */
    private $nomlecteur;

    public function getNumlecteur(): ?string
    {
        return $this->numlecteur;
    }
    public function setNumlecteur($numlecteur)
    {
        $this->numlecteur = $numlecteur;
    }
    public function getNomlecteur(): ?string
    {
        return $this->nomlecteur;
    }
    public function setNomlecteur($nomlecteur)
    {
        $this->nomlecteur = $nomlecteur;
    }
}
