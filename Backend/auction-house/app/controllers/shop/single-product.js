import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils';

export default Controller.extend({
  session: Ember.inject.service(),
  store: Ember.inject.service(),
  
  amountHasError: null,
  amountErrorMessage: null,
  currentPicture: null,
  bidValue: null,
  bidListSize: 5,
  checker: null,
  topBid: null,

  sortedBids: Ember.computed('model.product.bids.[]', function(){
    return this.get('model.product.bids').sortBy('amount').reverse();
  }),

  wishlist: Ember.computed('model.product.bids.[]', function(){
    const store = this.get('store');
    return this.store.findAll('wishlist', { reload: true });
  }).volatile(),

  userWishlistItem: Ember.computed('checker', 'wishlist.@each', function(){
    var _this = this;
    var one = this.get('wishlist').then(resolvedWishlist => {
      resolvedWishlist.forEach(item => {
       if(_this.get('checker') == null) {
          if(_this.get('model.product.id') === item.get('product').get('id')) {
            _this.set('checker', item);
          }
        }
      });
    });
  }).volatile(),

  actions: {

    createBid: function(productID) {
      var _this = this;

      let bid = this.store.createRecord('bid');
      bid.set('amount', this.get('bidAmount'));
      bid.set('product_id', productID);

      this.set('topBid', this.get('sortedBids.firstObject.amount'));

      if(isEmpty(this.get('topBid'))){
        this.set('topBid', this.get('model.product.startingPrice'));
      }

      var userBid = this.get('bidAmount');


      bid.validate().then(({ validations }) => {
        if(validations.get('isValid') && (userBid > _this.get('topBid')) && (userBid != _this.get('topBid'))){
          
          bid.save().then(function() {
            _this.set('bidAmount', null);
            _this.set('amountHasError', false);
            _this.set('amountErrorMessage', null);
            _this.get('flashMessages').success('Bid Created!');
            _this.get('model.product.bids').addObject(bid);
          }, function(response){
            _this.get('flashMessages').warning('An error occoured please try again!');
          });

        } else {

          if(bid.get('validations.attrs.amount.messages') !== '' && bid.get('validations.attrs.amount.messages') !== null){
            _this.set('amountHasError', true);
            _this.set('amountErrorMessage', bid.get('validations.attrs.amount.messages'));
          }

        }
      })
    },


    createWishlist: function(productID) {
      var _this = this;
      var wishlistCreate = this.store.createRecord('wishlist', {
        product_id: productID
      }).save().then(function(data){
        _this.set('checker', data);      
      }).catch(function(){

      });
    },


    deleteWishlist: function() {
      var _this = this;
      var currentWishlistItem = _this.get('checker');
      currentWishlistItem.destroyRecord().then(function(){
        _this.set('checker', null);  
      });
    },

    setCurrentPicture: function(picture){
      this.set('currentPicture', picture);
    },

    loadMore: function(){
      this.set('bidListSize', this.get('bidListSize') + 5);
    },

    clearFields: function(){
      this.set('bidAmount', null);
      this.set('currentPicture', null);
      this.set('amountHasError', null);
      this.set('amountErrorMessage', null);
      this.set('bidListSize', 5);
      this.set('checker', null);
    }
  }
});
