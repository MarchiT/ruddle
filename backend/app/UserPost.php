<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class UserPost extends Model
{
  public function posts() {
    return $this->hasMany(Post::class, 'id', 'post_id');
  }
}
