<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/


use App\User;

use Illuminate\Http\Request;

Route::get('/', function () {
  // dd(User::all());
    return view('welcome');
});

Auth::routes();

Route::get('/home', 'HomeController@index')->name('home');


Route::post('/register', 'SignInController@register');
Route::post('/login', 'SignInController@login');


Route::get('/posts/created/{user_id}', 'UserPostController@created');
Route::get('/posts/solved/{user_id}', 'UserPostController@solved');
Route::get('/posts/inprogress/{user_id}', 'UserPostController@inprogress');

Route::get('/posts/userpost', 'UserPostController@index');

Route::post('/userposts', 'UserPostController@create');


Route::post('/posts', 'PostController@store');
Route::get('/posts/{user_id}', 'PostController@index');
Route::get('/post/{post}', 'PostController@show');
