namespace MK8DXPlaylist.Services;

using Microsoft.JSInterop;
using System.Text.Json;

public class LocalStorageService(IJSRuntime jsRuntime)
{
    private static readonly JsonSerializerOptions Options = new() { PropertyNameCaseInsensitive = true };

    public async Task SetItemAsync<T>(string key, T value)
    {
        var json = JsonSerializer.Serialize(value, Options);
        await jsRuntime.InvokeVoidAsync("localStorage.setItem", key, json);
    }

    public async Task<T?> GetItemAsync<T>(string key)
    {
        var json = await jsRuntime.InvokeAsync<string?>("localStorage.getItem", key);
        if (string.IsNullOrEmpty(json)) return default;
        return JsonSerializer.Deserialize<T>(json, Options);
    }
}
