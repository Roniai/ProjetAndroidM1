<?php

namespace App\Entity;

use ApiPlatform\Core\Annotation\ApiResource;
use App\Repository\PretRepository;
use Doctrine\ORM\Mapping as ORM;

/**
 * @ApiResource(formats= {"json"})
 * @ORM\Entity(repositoryClass=PretRepository::class)
 */
class Pret
{
    /**
     * @ORM\Id
     * @ORM\Column(type="string", length=25)
     */
    private $numlecteur;
    /**
     * @ORM\Id
     * @ORM\Column(type="string", length=25)
     */
    private $numlivre;
    /**
     * @ORM\Column(type="string", length=25)
     */
    private $datepret;

    public function getNumlecteur(): ?string
    {
        return $this->numlecteur;
    }
    public function setNumlecteur($numlecteur)
    {
        $this->numlecteur = $numlecteur;
    }
    public function getNumlivre(): ?string
    {
        return $this->numlivre;
    }
    public function setNumlivre($numlivre)
    {
        $this->numlivre = $numlivre;
    }
    public function getDatepret(): ?string
    {
        return $this->datepret;
    }
    public function setDatepret($datepret)
    {
        $this->datepret = $datepret;
    }

    //Parameters : numlecteur=1;numlivre=L1
}
