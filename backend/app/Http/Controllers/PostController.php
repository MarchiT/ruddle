<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Post;
use App\UserPost;
use App\User;

class PostController extends Controller
{
  public function index($id) {
    $posts = Post::latest()->get()->all();
    // return $posts;
    return $this->putIntoArray($posts, $id);
  }

  public function putIntoArray($posts, $id) {
    $data = array();
    $i = 0;

    foreach ($posts as $post) {
      $creatorId = $post->postUserCreator->first();
      $user = User::find($creatorId->user_id);

      $item['id'] = $post->id;
      $item['title'] = $post->title;
      $item['body'] = $post->body;
      $item['answer'] = $post->answer;
      $item['name'] = $user->name;
      $item['email'] = $user->email;

      $relationUserCurrentPost = User::find($id)->userPosts->where('post_id', $post->id)->first();
      if ($relationUserCurrentPost != null)
        $item['tag'] = $relationUserCurrentPost->choices;
      else
        $item['tag'] = '';

      $data[$i++] = $item;
    }

    return $data;
  }


  public function show(Post $post) {
    return $post;
  }

  public function create() {
  }

  public function store() {
    $data = request()->json()->all();

    $createdPost = Post::create([
        'title' => $data['title'],
        'body' => $data['body'],
        'answer' => $data['answer'],
    ]);

    UserPost::create([
        'user_id' => (int)$data['id'],
        'post_id' => $createdPost['id'],
        'choices' => $data['tag'],
    ]);
  }

}
