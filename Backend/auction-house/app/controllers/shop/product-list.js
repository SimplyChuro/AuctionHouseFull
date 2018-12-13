import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils';
import { stringSimilarity } from "string-similarity-js";

export default Controller.extend({
  session: Ember.inject.service(),
  store: Ember.inject.service(),

  queryParams: ['name', 'parent_category', 'child_category', 'color', 'size', 'list_type' , 'sorting'],
  name: null,
  parent_category: null,
  child_category: null,
  sorting: null,
  color: null,
  size: null,
  list_type: null,
  checker: null,
  allWishlistItems: null,
  closestName: null,
  nameNotFound: false,

  wishlist: Ember.computed(function(){
    const store = this.get('store');
    return this.store.findAll('wishlist', { reload: true });
  }).volatile(),

  wishlistItems: Ember.computed('wishlist', function(){
    this.set('allWishlistItems', this.get('wishlist'));

    return this.get('allWishlistItems');
  }).volatile(),

  products: Ember.computed('name', 'parent_category', 'child_category', function(){
    var checker = this.get('child_category');
    if(isEmpty(checker)) {
      checker = 0;
    }
    return this.store.query('product', { name: this.get('name'), category: checker, featured: '', status: '', rating: '' });
  }),


  filteredProducts: Ember.computed('name', 'parent_category', 'child_category', 'color', 'size', 'sorting', function(){
 
    var products = this.get('products');
    var _this = this;
    let singleProduct = null;

    if(!(isEmpty(this.get('color')))) {
      products = products.filter(
        function(product) { 
          if(product.color == _this.get('color')) {
            singleProduct = product;
          } else {
            singleProduct = null;
          }
          return singleProduct;
        }
      );
    }

    if(!(isEmpty(this.get('size')))) {
      products = products.filter(
        function(product) { 
          if(product.size == _this.get('size')) {
            singleProduct = product;
          } else {
            singleProduct = null;
          }
          return singleProduct;
        }
      );
    }

    if(this.get('sorting') == 'popularity'){
      return products.sortBy('bids.length').reverse();
    }

    if(this.get('sorting') == 'rating-high'){
      return products.sortBy('averageScore').reverse();
    }

    if(this.get('sorting') == 'rating-low'){
      return products.sortBy('averageScore');
    }

    if(this.get('sorting') == 'newest'){
      return products.sortBy('publishDate');
    }

    if(this.get('sorting') == 'oldest'){
      return products.sortBy('expireDate');
    }

    if(this.get('sorting') == 'price-low'){
      return products.sortBy('startingPrice');
    }

    if(this.get('sorting') == 'price-high'){
      return products.sortBy('startingPrice').reverse();
    }

    return products;

  }),

  productNameErrorMessage: Ember.computed('filteredProducts', function(){

    var nameChecker = null;
    var similarityRate = 0;
    var _this = this;

    if(isEmpty(this.get('filteredProducts')) && !(isEmpty(this.get('name')))){
      var categories = this.get('model.categoryList');
      _this.set('closestName', null);
      categories.forEach((item, index) => {
        if(stringSimilarity(item.name, this.get('name')) > similarityRate){
          similarityRate = stringSimilarity(item.name, this.get('name'));
          nameChecker = item.name;
          _this.set('closestName', nameChecker);
        }
      });

      _this.set('nameNotFound', true);
    
      }
      return _this.get('nameNotFound');
  }),

  actions: {
    toggleDetails: function(category) {

      if(category.id !== this.get('parent_category')){
        this.set('parent_category', category.id);
      } else {
        this.set('parent_category', null);
        this.set('child_category', null);
      }

    },

    selectCategory: function(selected) {
      if(selected === null){
        this.set('child_category', selected);  
        this.set('parent_category', selected);  
      } else {
        this.set('child_category', selected.id);  
      }
    },

    setColor: function(color) {
      this.set('color', color);
    },

    setSize: function(size) {
      this.set('size', size);  
    },

    setListType: function(type) {
      this.set('list_type', type);  
    },

    setSortType: function(type){
      this.set('sorting', type);  
    },

    createWishlist: function(productID) {
      var _this = this;
      this.store.createRecord('wishlist', {
        status: 'active',
        product_id: productID
      }).save();
    },

    deleteWishlist: function(wishlistItem) {
      var _this = this;
      var currentWishlistItem = wishlistItem;
      currentWishlistItem.destroyRecord().then(function(){
        _this.get('allWishlistItems').removeObject(wishlistItem);
      });
    },

    clearFields: function(){
      this.set('name', undefined);
      this.set('parent_category', undefined);
      this.set('child_category', undefined);
      this.set('sorting', undefined)
      this.set('color', undefined); 
      this.set('size', undefined); 
      this.set('list_type', undefined); 
      this.set('closestName', null); 
      this.set('checker', null);
      this.set('allWishlistItems', null);
      this.set('closestName', null);
      this.set('nameNotFound', false);
    }
  }
});
