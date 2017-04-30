<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Post extends Model
{
  protected $fillable = [
      'title', 'body', 'answer'
  ];
    // public function user() {
    //   return $this->belongsTo(Post::class);
    // }
    //
    // public function comments() {
    //   return $this->hasMany(Comment::class);
    // } in class Post
}
