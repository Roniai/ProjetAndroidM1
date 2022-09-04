<?php

namespace App\Entity;

use ApiPlatform\Core\Annotation\ApiResource;
use App\Repository\LivreRepository;
use Doctrine\ORM\Mapping as ORM;

/**
 * @ApiResource()
 * @ORM\Entity(repositoryClass=LivreRepository::class)
 */
class Livre
{
    /**
     * @ORM\Id
     * @ORM\Column(type="string", length=25)
     */
    private $numlivre;
    /**
     * @ORM\Column(type="string", length=50)
     */
    private $designlivre;
    /**
     * @ORM\Column(type="string", length=30)
     */
    private $autlivre;
    /**
     * @ORM\Column(type="string", length=25)
     */
    private $dateeditlivre;
    /**
     * @ORM\Column(type="string", length=5)
     */
    private $dispolivre;

    public function getNumlivre(): ?string
    {
        return $this->numlivre;
    }
    public function setNumlivre($numlivre)
    {
        $this->numlivre = $numlivre;
    }
    public function getDesignlivre(): ?string
    {
        return $this->designlivre;
    }
    public function setDesignlivre($designlivre)
    {
        $this->designlivre = $designlivre;
    }
    public function getAutlivre(): ?string
    {
        return $this->autlivre;
    }
    public function setAutlivre($autlivre)
    {
        $this->autlivre = $autlivre;
    }
    public function getDateeditlivre(): ?string
    {
        return $this->dateeditlivre;
    }
    public function setDateeditlivre($dateeditlivre)
    {
        $this->dateeditlivre = $dateeditlivre;
    }
    public function getDispolivre(): ?string
    {
        return $this->dispolivre;
    }
    public function setDispolivre($dispolivre)
    {
        $this->dispolivre = $dispolivre;
    }
}
