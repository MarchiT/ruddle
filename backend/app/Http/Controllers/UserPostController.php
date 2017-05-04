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

  public function create() {
    $data = request()->json()->all();

    UserPost::create([
        'user_id' => (int)$data['user_id'],
        'post_id' => (int)$data['post_id'],
        'choices' => $data['tag'],
    ]);
  }


  public function created($user_id) {
    $user = User::find($user_id);
    $user_posts = $user->userPosts->where('choices', 'created');

    return $this->putIntoArray($user_posts);
  }

  public function solved($user_id) {
    $user_posts = User::find($user_id)->userPosts->where('choices', 'solved');
    return $this->putIntoArray($user_posts);
  }

  public function inprogress($user_id) { //1.return an error if tag missing 2. doesnt find helper function
    return $this->putIntoArray(User::find($user_id)->userPosts->where('choices', 'inprogress'));
  }


  public function putIntoArray($user_posts) {
    $data = array();
    $i = 0;

    foreach ($user_posts as $up) {
      $data[$i++] = $up->posts->first();
    }

    return $data;
  }
}
