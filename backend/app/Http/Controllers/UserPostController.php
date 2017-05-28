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

    $toChange = UserPost::where('user_id', (int)$data['user_id'])->get()
                        ->where('post_id', (int)$data['post_id'])->first();

    if ($toChange == null) {
      UserPost::create([
          'user_id' => (int)$data['user_id'],
          'post_id' => (int)$data['post_id'],
          'choices' => $data['tag'],
      ]);
      return "you came here buddy";
    }
    else {
      if ((string)$data['tag'] == 'nothing')
        $toChange->choices = null;
      else
        $toChange->choices = (string)$data['tag'];

      $toChange->save();
    }
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

  public function inprogress($user_id) {
    return $this->putIntoArray(User::find($user_id)->userPosts->where('choices', 'inprogress'));
  }


  public function putIntoArray($user_posts) {
    $data = array();
    $i = 0;

    foreach ($user_posts as $up) {
      $item = $up->posts->first();
      $user = $up->users->first();
      // $item['choices'] = $user_posts->first()['choices'];
      $item['name'] = $user['name'];
      $item['email'] = $user['email'];

      $data[$i++] = $item;
    }

    return $data;
  }
}
