<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\UserPost;
use App\User;


class UserPostController extends Controller
{
  public function index() {
    $user_posts = UserPost::get()->all();
    return $user_posts;
  }

  public function created($user_id) {
    $user = User::find($user_id);
    return ($user->posts->where('choices', 'created')->sortByDesc('created_at')); //why doesn't it sort them properly
  }

  public function solved($user_id) {
    return User::find($user_id)->posts->where('choices', 'solved');
  }

  public function inprogress($user_id) {
    return User::find($user_id)->posts->where('choices', 'inprogress');
  }
}
