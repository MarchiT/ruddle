<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Post extends Model
{
  protected $fillable = [
      'title', 'body', 'answer'
  ];


  public function postUserCreator() {
    $array = $this->hasMany(UserPost::class)->where('choices', 'created');

    return $array;

  }
}
