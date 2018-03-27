
import React from "react"
import { compose, withProps, withStateHandlers } from "recompose"
import { withScriptjs, withGoogleMap, GoogleMap, Marker, InfoWindow } from "react-google-maps"

//the set up of google maps in react was built using documentation provided by https://tomchentw.github.io/react-google-maps/#introduction
//methods declared below help set up initial props necessary for the basic rendering of the google map component
//as well as methods to set state in order to render an info window on the markers
const MyMapComponent = compose(
  withProps({
    googleMapURL: "https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=geometry,drawing,places",
    loadingElement: <div style={{ height: `100%` }} />,
    containerElement: <div style={{ height: `600px` }} />,
    mapElement: <div style={{ height: `100%` }} />,
  }),
  withStateHandlers(() => ({
    isOpen: false,
  }), {
    onToggleOpen: ({ isOpen }) => () => ({
      isOpen: !isOpen,
    }),
    showInfo: ({ a }) => () => ({
        showInfoIndex: a,
      })
  }),
  withScriptjs,
  withGoogleMap
  //below features the default map zones and the markers being mapped over to display different geo locations and info windows
)((props) =>
  <GoogleMap
    defaultZoom={14}
    defaultCenter={{ lat: 40.7128, lng: -74.0060 }}
  >
  {props.favs.map((marker, index) => (
    <Marker 
        key={index}
        position={{ 
            lat: marker[0],
            lng: marker[1]
        }} 
        onClick={props.onToggleOpen} 
    >
    {props.isOpen && <InfoWindow onCloseClick={props.onToggleOpen}>
        <div>{marker[2]}</div>
      </InfoWindow>}
      </Marker>
    ))}
  </GoogleMap>
)

export default MyMapComponent;