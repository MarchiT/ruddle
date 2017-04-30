<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use Illuminate\Hashing\BcryptHasher;

use App\User;

use Hash;


class SignInController extends Controller
{

  public function register() {
    $data = request()->json()->all();

    User::create([
        'name' => $data['name'],
        'email' => $data['email'],
        'password' => bcrypt($data['password']),
    ]);
  }

  public function login(Request $request) {
    $data = $request->json()->all();

    $user = User::where('email', $data['email'])->first();

    if (Hash::check($data['password'], $user['password'])) {
      return (string)$user;
    }

    return "Wrong pass";
  }

}
