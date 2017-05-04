<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class UserPost extends Model
{
  protected $fillable = [
    'user_id', 'post_id', 'choices',
  ];


  public function posts() {
    return $this->hasMany(Post::class, 'id', 'post_id');
  }
}
