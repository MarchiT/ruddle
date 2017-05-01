<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Post;


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

    Post::create([
        'title' => $data['title'],
        'body' => $data['body'],
        'answer' => $data['answer'],
    ]);
  }
}
