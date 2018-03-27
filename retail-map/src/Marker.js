import React from "react";
import MyMapComponent from './Map';

class MyFancyComponent extends React.PureComponent {
  constructor(props) {
    super(props);
    this.state = {}
  }
  
  render() {
    let favs;
    let results;
    console.log('selected list', this.props.selected)
    console.log('fav list', this.props.favorites)

    //conditional rendering to see if results exist from query external API search
    //if results exist, map over the data and return location data for map component
    //if false, map over favorites location data
    if(this.props.selectedList) {
      results = this.props.selected.map((selected, index) => {
        return ([
          parseFloat(selected.latitude),
          parseFloat(selected.longitude),
          selected.cnbio_org_name
        ])
      })
    } else {
      favs = this.props.favorites.map((favorite, index) => {
        return ([
          parseFloat(favorite.lat),
          parseFloat(favorite.lon),
          favorite.orgName
        ])
      })
    }

    return (
      <div>
      {this.props.selectedList ?
      <MyMapComponent
        isMarkerShown={this.state.isMarkerShown}
        onMarkerClick={this.handleMarkerClick}
        favs={results}
      />
        :
      <MyMapComponent
        isMarkerShown={this.state.isMarkerShown}
        onMarkerClick={this.handleMarkerClick}
        favs={favs}
      />
      }
      </div>
    )
  }
}

export default MyFancyComponent;