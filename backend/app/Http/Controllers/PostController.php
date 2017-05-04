<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Post;
use App\UserPost;


class PostController extends Controller
{
  public function index() {
    $posts = Post::latest()->get()->all();
    return $posts;
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
