FROM mcr.microsoft.com/dotnet/sdk:10.0 AS build
WORKDIR /src
COPY WebVersion/MK8DXPlaylist/MK8DXPlaylist.csproj WebVersion/MK8DXPlaylist/
RUN dotnet restore WebVersion/MK8DXPlaylist/MK8DXPlaylist.csproj
COPY WebVersion/MK8DXPlaylist/ WebVersion/MK8DXPlaylist/
RUN dotnet publish WebVersion/MK8DXPlaylist/MK8DXPlaylist.csproj -c Release -o /app/publish

FROM nginx:alpine AS final
COPY --from=build /app/publish/wwwroot /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
