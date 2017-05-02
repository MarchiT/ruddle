<?php

namespace App;

use Illuminate\Notifications\Notifiable;
use Illuminate\Foundation\Auth\User as Authenticatable;

class User extends Authenticatable
{
    use Notifiable;

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'name', 'email', 'password',
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        'password', 'remember_token',
    ];


    public function userPosts() {
      $array = $this->hasMany(UserPost::class); //TODO find a way to sort them by created_at

      // $array = array_values(array_sort($array, function ($value) {
      //     return $value['created_at'];
      // }));
      return $array;

    }
}
