<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class UserPost extends Model
{

  protected $primaryKey = ['user_id', 'post_id'];
  public $incrementing = false;

  protected $fillable = [
    'user_id', 'post_id', 'choices',
  ];

  public function posts() {
    return $this->hasMany(Post::class, 'id', 'post_id');
  }

  public function users() {
    return $this->hasMany(User::class, 'id', 'user_id');
  }
}
