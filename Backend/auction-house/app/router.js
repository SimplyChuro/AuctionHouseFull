import EmberRouter from '@ember/routing/router';
import config from './config/environment';

const Router = EmberRouter.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function() {
  this.route('error', {path: '/*wildcard'});
  this.route('login');
  this.route('register');
  this.route('home', function() {});
  this.route('shop', function() {
    this.route('single-product', { path: 'single-product/:product_id' });
    this.route('about-us');
    this.route('terms-and-conditions');
    this.route('privacy-and-policy');
  });
  this.route('account');
  this.route('password-reset');
  this.route('category-list', { path: 'home/category-list' });
});

export default Router;
